import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Monitor {

    private RdP RedDePetri;
    private Semaphore mutex;
    private boolean finalizo;
    private Politica politica;
    private Semaphore[] colas;
    private boolean[] transSensConHilos;

    private int contador; //Se usa para el control de los 1000 invariantes que deben completarse

    private ArrayList<Integer> trDisparables;

    public Monitor(RdP redDePetri,Politica politica) {
        RedDePetri = redDePetri;
        transSensConHilos = new boolean[Main.getCantT()];

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
                int tr = politica.determinarTr(transSensConHilos);
                colas[tr].release();
            }else{
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Devuelve si hay alguna transicion sensibilizada que tenga hilos esperando, y pueda ser disparada.
     * @return boolean
     */
    public boolean hayHilosEsperando(){

        boolean[] sensibilizadas = RedDePetri.getSensibilizadas();
        boolean[] hilosEnColas = getHilosEnColas();


        for (int i = 0; i < sensibilizadas.length; i++) {
            transSensConHilos[i] = hilosEnColas[i] && sensibilizadas[i];
        }

        return politica.sonDisparables(transSensConHilos);
    }


    private boolean[] getHilosEnColas(){
        boolean[] hilosEnCola = new boolean[Main.getCantT()];

        for (int i = 0; i < colas.length; i++) {
            hilosEnCola[i] = colas[i].hasQueuedThreads();
        }
        return hilosEnCola;
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
