import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private RdP RedDePetri; //Red a manejar
    private ReentrantLock mutex; //Solo un hilo puede entrar al monitor a la vez

    private Condition espera_temporales;
    private boolean finalizo;
    private Politica politica;
    private Condition[] colas;

    private int contador; //Se usa para el control de los 1000 invariantes que deben completarse

    public Monitor(RdP redDePetri,Politica politica) {
        RedDePetri = redDePetri;
        mutex = new ReentrantLock();

        espera_temporales=mutex.newCondition();
        colas=new Condition[Main.getCantT()];

        for (int i = 0; i < colas.length; i++) {
            colas[i]=mutex.newCondition();
        }

        this.politica=politica;
        finalizo=false;
    }

    public void disparar(int transicion){ //Ejecutado por cada hilo que quiera disparar una transicion
        try {
            mutex.lock();


            while (!(RedDePetri.isHabilitada(transicion))) { //Si la transicion deseada no esta habilitada

                if(RedDePetri.esTemporal(transicion) && RedDePetri.getTiempoDeSensibilizacion(transicion) !=0) { //Si es porque aun no se cumplio el tiempo

                    long tiempoActual = System.currentTimeMillis();
                    long intervalo = RedDePetri.getTiempoDeTransicion(transicion) - (tiempoActual - RedDePetri.getTiempoDeSensibilizacion(transicion));
                    if(intervalo > 0) {
                        despertar();
                        espera_temporales.await(intervalo, TimeUnit.MILLISECONDS); //Espera el tiempo restante
                    }
                    RedDePetri.habilitacion();
                }
                else { //Si es por no tener tokens/recurso
                    despertar();
                    colas[transicion].await();//El hilo espera en la cola de espera de su transicion
                }
            }

            RedDePetri.disparo(transicion); // Cuando este habilitada, se dispara la transicion

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            despertar(); //Antes de salir del monitor, despierta al siguiente hilo
            mutex.unlock(); //Libera el lock
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
        if(!finalizo){ //Si ya se finalizo, no se tiene en cuenta la politica
            ArrayList<Integer> despertarTr = politica.determinarTr(); //La politica determina que invariantes pueden ser ejecutados
            for (int i = 0; i < despertarTr.size(); i++) {
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
    }
}
