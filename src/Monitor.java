import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private RdP RedDePetri; //Red a manejar
    private ReentrantLock mutex; //Solo un hilo puede entrar al monitor a la vez

    private boolean finalizo;
    private Politica politica;
    private Condition[] colas;


    private int contador; //Se usa para el control de los 1000 invariantes que deben completarse

    public Monitor(RdP redDePetri,Politica politica) {
        RedDePetri = redDePetri;
        mutex = new ReentrantLock();

        colas=new Condition[Main.getCantT()];

        for (int i = 0; i < colas.length; i++) {
            colas[i]=mutex.newCondition();
        }

        this.politica=politica;
        finalizo=false;
    }

    public boolean disparar(int transicion){ //Ejecutado por cada hilo que quiera disparar una transicion
        try {
            mutex.lock();

            while (!(RedDePetri.estaSensibilizada(transicion)))//en caso de que la transicion no este sensibilizada
                colas[transicion].await();

            if (RedDePetri.chequeoVentanaTiempo(transicion)) { //chequeo de la ventana
                //ventana==true
                if(RedDePetri.checkEsperando(transicion)){  //si estoy dentro, se chequea si no hay alguno ya esperando
                    //esperando==true
                    colas[transicion].await();
                }
            }else{
                //ventana==false
                RedDePetri.setEsperando(transicion);
                mutex.unlock();
                return false;
            }

            RedDePetri.disparo(transicion); //se dispara la transicion, ejecuta actMarcado() y actSensibilziadas()

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        despertar(); //señaliza al siguiente hilo, utilizando la politica
        mutex.unlock(); //libera el lock
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

    public void despertar(){
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
    }

}
