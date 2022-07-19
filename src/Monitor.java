import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Monitor {

    private RdP RedDePetri;
    private Semaphore mutex;
    private boolean finalizo;
    private Politica politica;
    private Semaphore[] colas;

    private int contador; //Se usa para el control de los 1000 invariantes que deben completarse

    private ArrayList<Integer> trDisparables;

    public Monitor(RdP redDePetri,Politica politica) {
        RedDePetri = redDePetri;

        mutex = new Semaphore(1);

        colas = new Semaphore[Main.getCantT()];
        for (int i = 0; i < colas.length; i++) {
            colas[i]=new Semaphore(0);
        }

        this.politica=politica;
        finalizo=false;
    }

    public boolean disparar(int transicion){ //Ejecutado por cada hilo que quiera disparar una transicion

        try {
//            System.out.println("ENTRO A DISPARAR " + transicion);
            mutex.acquire();

            if (!RedDePetri.estaSensibilizada(transicion)){
                //en caso de que la transicion no este sensibilizada, espera por el recurso
                mutex.release();
                colas[transicion].acquire();
            }
            if(RedDePetri.checkEsperando(transicion)){
                //en caso de que un hilo ya este esperando por esa transicion, espera por el recurso
                mutex.release();
                colas[transicion].acquire();
            }
            if (RedDePetri.antesVentanaTiempo(transicion)){
                //en caso de estar antes de la ventana, se setea esperando y sale
                RedDePetri.setEsperando(transicion);
                mutex.release();
                return false;
            }

            //si se cumplieron las condiciones, dispara la transicion
            RedDePetri.disparo(transicion); //se dispara la transicion, ejecuta actMarcado() y actSensibilziadas()

            //determina si despertar a otro hilo o solo salir del monitor
            if(hayHilosEsperando()){
                Integer tr = cualDespertar();
                colas[tr].release();
            }else{
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean hayHilosEsperando(){

        trDisparables = politica.determinarTr();

        for (int i = 0; i < trDisparables.size(); i++) {
            Integer trDisparable = trDisparables.get(i);
            if(colas[trDisparable].hasQueuedThreads()){
                return true;
            }
        }
        return false;
    }

    public Integer cualDespertar(){

        for (int i = 0; i < trDisparables.size(); i++) {
            Integer tr = trDisparables.get(i);
            if(colas[tr].hasQueuedThreads()){
                return tr;
            }
        }
        return -1;
    }

    public synchronized void incContador(){
        contador ++;
    }

    public synchronized int getContador() {
        return contador;
    }

    public void finalizar(){
        finalizo=true;
    }

}
