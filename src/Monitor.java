import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Monitor {

    private RdP RedDePetri; //Red a manejar
//    private ReentrantLock mutex; //Solo un hilo puede entrar al monitor a la vez
    private Semaphore mutex;
    private boolean finalizo;
    private Politica politica;
//    private Condition[] colas;
    private Semaphore[] colas;

    private int contador; //Se usa para el control de los 1000 invariantes que deben completarse

    private ArrayList<Integer> trDisparables;

    public Monitor(RdP redDePetri,Politica politica) {
        RedDePetri = redDePetri;
        /*mutex = new ReentrantLock();

        colas=new Condition[Main.getCantT()];*/

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

            if (!(RedDePetri.estaSensibilizada(transicion)) || RedDePetri.checkEsperando(transicion) ){//en caso de que la transicion no este sensibilizada
                mutex.release();
//                System.out.println("NO ESTA SENSIBILIZADA TRANSICION" + transicion);
                colas[transicion].acquire();
            }

            if (RedDePetri.antesVentanaTiempo(transicion)) { //chequeo de la ventana
                RedDePetri.setEsperando(transicion);
                mutex.release();
                return false;
            }
//            System.out.println("DISPARE TRANSICION" + transicion);
            RedDePetri.disparo(transicion); //se dispara la transicion, ejecuta actMarcado() y actSensibilziadas()

            if(hayHilosEsperando()){
                Integer tr = cualDespertar();
                colas[tr].release();
            }else{
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        despertar(); //señaliza al siguiente hilo, utilizando la politica
//       OJOTAAA mutex.unlock(); //libera el lock
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

    /*    public void despertar(){
        if(!finalizo){ //Si ya se finalizo, no se tiene en cuenta la politica
            ArrayList<Integer> despertarTr = politica.determinarTr(); //La politica determina que invariantes pueden ser ejecutados

            for (int i = 0; i < despertarTr.size(); i++) {
                System.out.print(despertarTr.get(i));
            }
            System.out.println("");

            for (int i = 0; i < despertarTr.size(); i++) {
                //System.out.println(mutex.hasWaiters(colas[despertarTr.get(i)]));
                if(mutex.hasWaiters(colas[despertarTr.get(i)])){
                    colas[despertarTr.get(i)].signal();
                    break;
                }
            }

        }else {
            for (int i = 0; i < Main.getCantT(); i++) {
                colas[i].signalAll();
            }
        }
    }*/
}
