import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private RdP RedDePetri; //Red a manejar
//    private ReentrantLock mutex; //Solo un hilo puede entrar al monitor a la vez
    private Semaphore mutex;
    private boolean finalizo;
    private Politica politica;
//    private Condition[] colas;
    private Semaphore[] colas;

    private int contador; //Se usa para el control de los 1000 invariantes que deben completarse

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

            while (!(RedDePetri.estaSensibilizada(transicion))){//en caso de que la transicion no este sensibilizada
                mutex.release();
//                System.out.println("NO ESTA SENSIBILIZADA TRANSICION" + transicion);
                colas[transicion].acquire();
            }

            if (RedDePetri.chequeoVentanaTiempo(transicion)) { //chequeo de la ventana
                //ventana==true
                if(RedDePetri.checkEsperando(transicion)){  //si estoy dentro, se chequea si no hay alguno ya esperando
                    //esperando==true
                    mutex.release();
//                    System.out.println("HAY UNO ESPERANDO TRANSICION" + transicion);

                    colas[transicion].acquire();
                }
            }else{
                //ventana==false
//                System.out.println("NO SE CUMPLIO TIEMPO TRANS " + transicion);
                RedDePetri.setEsperando(transicion);
                mutex.release();
                return false;
            }
//            System.out.println("DISPARE TRANSICION" + transicion);
            RedDePetri.disparo(transicion); //se dispara la transicion, ejecuta actMarcado() y actSensibilziadas()
            if(hayHilosEsperando()){
                Integer tr = transicionDisparable();
                if(tr == -1){
                    mutex.release();
                    return true;
                }
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

    public boolean hayHilosEsperando(){
        for (int i = 0; i < colas.length; i++) {
            if(colas[i].hasQueuedThreads() && RedDePetri.estaSensibilizada(i)){
                return true;
            }
        }
        return false;
    }

    public Integer transicionDisparable(){
        ArrayList<Integer> despertarTr = politica.determinarTr();
        if(despertarTr.size() == 0){
            System.out.println("ARRAY DE TRANSICIONES VACIO");
        }
        for (int i = 0; i < despertarTr.size(); i++) {
            Integer tr = despertarTr.get(i);
            if(colas[tr].hasQueuedThreads()){
                return tr;
            }
        }
        return -1;
    }

}
