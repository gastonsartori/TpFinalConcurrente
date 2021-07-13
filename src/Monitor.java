import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private RdP RedDePetri;
    private ReentrantLock mutex;
    private Condition espera;

    private int contador;



    public Monitor(RdP redDePetri) {
        RedDePetri = redDePetri;
        mutex = new ReentrantLock();
        espera=mutex.newCondition();

    }

    public void disparar(int transicion) throws InterruptedException{
        try {
            mutex.lock();

            while (!(RedDePetri.isHabilitada(transicion))) {

                espera.await();
            }

            RedDePetri.disparo(transicion);

            espera.signalAll();

        }finally {
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
