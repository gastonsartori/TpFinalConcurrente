import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private RdP RedDePetri;
    private ReentrantLock mutex;
    private Condition espera;

    public Monitor(RdP redDePetri) {
        RedDePetri = redDePetri;
        mutex = new ReentrantLock();
        espera=mutex.newCondition();
    }

    public void disparar(int transicion){

        mutex.lock();

        while(!(RedDePetri.isHabilitada(transicion))){
            try {
                System.out.println("no me habilitaron :(");
                espera.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        RedDePetri.disparo(transicion);

        espera.signalAll();

        mutex.unlock();
    }
}
