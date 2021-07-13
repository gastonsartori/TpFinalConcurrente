import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private RdP RedDePetri;
    private ReentrantLock mutex;
    private Condition espera1;
    private Condition espera2;
    private Condition espera3;
    private Condition espera4;

    private int contador;



    public Monitor(RdP redDePetri) {
        RedDePetri = redDePetri;
        mutex = new ReentrantLock();
        espera1=mutex.newCondition();
        espera2=mutex.newCondition();
        espera3=mutex.newCondition();
        espera4=mutex.newCondition();

    }

    public void disparar(int transicion){
        try {
            mutex.lock();

            while (!(RedDePetri.isHabilitada(transicion))) {
                if(Thread.currentThread().getName()=="linea1"){
                    espera1.await();
                    System.out.println("espera1");
                }else if (Thread.currentThread().getName()=="linea2"){
                    espera2.await();
                    System.out.println("espera2");
                }else if (Thread.currentThread().getName()=="linea3"){
                    espera3.await();
                    System.out.println("espera3");
                }else if (Thread.currentThread().getName()=="linea4"){
                    espera4.await();
                    System.out.println("espera4");
                }
            }

            RedDePetri.disparo(transicion);

            espera1.signalAll();
            espera2.signalAll();
            espera3.signalAll();
            espera4.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.unlock();
        }
    }

    public synchronized void incContador(){
        contador ++;
    }

    public synchronized int getContador() {
        return contador;
    }
}
