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
        contador=0;
    }

    public void disparar(int transicion){

        mutex.lock();

        while(!(RedDePetri.isHabilitada(transicion))){
            try {
                espera.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        RedDePetri.disparo(transicion);

        contador++;
        System.out.println(contador);

        espera.signalAll();

        mutex.unlock();
    }

    public int getContador(){
        return contador;
    }


}
