import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private RdP RedDePetri; //red a manejar
    private ReentrantLock mutex; //solo un hilo puede entrar al monitor a la vez
    private Condition espera1;
    private Condition espera2;
    private Condition espera3;
    private Condition espera4;
    private boolean finalizo;
    private Politica politica;
    private Condition[] colas;

    private int contador; //se usa para el control de los 1000 invariantes que debn completarse

    public Monitor(RdP redDePetri,Politica politica) {
        RedDePetri = redDePetri;
        mutex = new ReentrantLock();
        espera1=mutex.newCondition();
        espera2=mutex.newCondition();
        espera3=mutex.newCondition();
        espera4=mutex.newCondition();
        colas= new Condition[]{espera1, espera2, espera3, espera4};
        this.politica=politica;
        finalizo=false;
    }

    public void disparar(int transicion){ //ejecuado por cada hilo que quiera disparar una transicion
        try {
            mutex.lock();

            while (!(RedDePetri.isHabilitada(transicion))) { //si la transicion deseada no esta habilitada
                //el hilo ejecutar despertar y espera en la cola de su invaraiante
                if(Thread.currentThread().getName()=="linea1"){
                    despertar();
                    espera1.await();
                    //System.out.println("espera1");
                }else if (Thread.currentThread().getName()=="linea2"){
                    despertar();
                    espera2.await();
                    //System.out.println("espera2");
                }else if (Thread.currentThread().getName()=="linea3"){
                    despertar();
                    espera3.await();
                    //System.out.println("espera3");
                }else if (Thread.currentThread().getName()=="linea4"){
                    despertar();
                    espera4.await();
                    //System.out.println("espera4");
                }
            }

            RedDePetri.disparo(transicion); // si estaba habilitada, se dispara la transicion

            despertar(); //antes de salir del monitor, despierta a los demas hilos

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.unlock(); //libera el lock
        }
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

    public void despertar(){

        if(!finalizo){ //si ya se finalizo, no se tiene en cuenta la politica
            boolean[] invariantes = politica.determinarInv(); //la politica determina que invaraintes pueden ser ejecutados

            for (int i = 0; i < 4; i++) {
                if(invariantes[i]){
                    colas[i].signalAll(); //despertar a todos los hilos de los invariantes que puedan ser ejectuados
                }
            }

        }else {
            espera1.signalAll();
            espera2.signalAll();
            espera3.signalAll();
            espera4.signalAll();
        }
    }
}
