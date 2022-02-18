import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private RdP RedDePetri; //red a manejar
    private ReentrantLock mutex; //solo un hilo puede entrar al monitor a la vez
    private Condition espera1;
    private Condition espera2;
    private Condition espera3;
    private Condition espera4;
    private Condition espera_temporales;
    private boolean finalizo;
    private Politica politica;
    private Condition[] colas;

    private int contador; //se usa para el control de los 1000 invariantes que debn completarse

    public Monitor(RdP redDePetri,Politica politica) {
        RedDePetri = redDePetri;
        mutex = new ReentrantLock();
        //espera1=mutex.newCondition();
        //espera2=mutex.newCondition();
        //espera3=mutex.newCondition();
        //espera4=mutex.newCondition();
        espera_temporales=mutex.newCondition();
        colas=new Condition[Main.getCantT()];

        //colas= new Condition[]{espera1, espera2, espera3, espera4};
        for (int i = 0; i < colas.length; i++) {
            colas[i]=mutex.newCondition();
        }

        this.politica=politica;
        finalizo=false;
    }

    public void disparar(int transicion){ //ejecuado por cada hilo que quiera disparar una transicion
        try {
            mutex.lock();
            //System.out.println("entrada=" + System.currentTimeMillis());
            //System.out.println(transicion);

            while (!(RedDePetri.isHabilitada(transicion))) { //si la transicion deseada no esta habilitada

                if(RedDePetri.esTemporal(transicion) && RedDePetri.getTiempoDeSensibilizacion(transicion) !=0){

                    long tiempoActual = System.currentTimeMillis();
                    long intervalo = RedDePetri.getTiempoDeTransicion(transicion) - (tiempoActual - RedDePetri.getTiempoDeSensibilizacion(transicion));
                   // System.out.println(intervalo+"="+RedDePetri.getTiempoDeTransicion(transicion)+"-"+ tiempoActual+"-"+ RedDePetri.getTiempoDeSensibilizacion(transicion));
                    if(intervalo > 0) {
                        despertar();
                        //System.out.println("me wa mimir por" + intervalo + Thread.currentThread().getName() + "desde" + System.currentTimeMillis());
                        espera_temporales.await(intervalo, TimeUnit.MILLISECONDS);
                        //System.out.println("me desperte" + Thread.currentThread().getName() + "a los" + System.currentTimeMillis());
                        //System.out.println(System.currentTimeMillis());
                    }
                    RedDePetri.habilitacion();
                    //System.out.println(System.currentTimeMillis());
                    //TODO: revisar la habilitacion de las temporales, se clava

                }else{

                    despertar();
                    //System.out.print("a esperar no tengo tokens ");
                    //System.out.println(System.currentTimeMillis());
                    colas[transicion].await();
                    //System.out.println("desperte de tokerns " + transicion);
                    //RedDePetri.habilitacion();

                    /*
                    if(Thread.currentThread().getName()=="linea1"){
                        //System.out.println("espera1");
                        despertar();
                        espera1.await();
                    }else if (Thread.currentThread().getName()=="linea2"){
                        //System.out.println("espera2");
                        despertar();
                        espera2.await();
                    }else if (Thread.currentThread().getName()=="linea3"){
                        //System.out.println("espera3");
                        despertar();
                        espera3.await();
                    }else if (Thread.currentThread().getName()=="linea4"){
                        //System.out.println("espera4");
                        despertar();
                        espera4.await();
                    }
                    */
                }
                //el hilo ejecutar despertar y espera en la cola de su invaraiante
            }

            RedDePetri.disparo(transicion); // si estaba habilitada, se dispara la transicion
            //RedDePetri.printTiempos();
            //System.out.println("disparo: " + System.currentTimeMillis());

            despertar(); //antes de salir del monitor, despierta a los demas hilos

            //System.out.println("desperte: " + System.currentTimeMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //System.out.println("salida=" + System.currentTimeMillis());
            mutex.unlock(); //libera el lock
        }
    }

    public synchronized void incContador(){
        contador ++;
    }

    public synchronized int getContador() {
        return contador;
    }

    public Condition[] getColas() {
        return colas;
    }

    public void finalizar(){
        finalizo=true;
    }

    public void despertar(){
        //System.out.println("despertar");
        if(!finalizo){ //si ya se finalizo, no se tiene en cuenta la politica
            ArrayList<Integer> despertarTr = politica.determinarTr(); //la politica determina que invaraintes pueden ser ejecutados
            boolean desperte = false;
            for (int i = 0; i < despertarTr.size(); i++) {
                if(mutex.hasWaiters(colas[despertarTr.get(i)])){
                    //System.out.println("despertar" + i);
                    colas[despertarTr.get(i)].signal();
                    desperte=true;
                    break;
                }
            }
            if(!desperte) // si no desperto a ningun hilo, despierto a un hilo temporal que durmio por falta de recursos.
                for (int i = 0; i < RedDePetri.cantT; i++) {
                    if(RedDePetri.esTemporal(i) && mutex.hasWaiters(colas[i])){
                        colas[i].signal();
                    }
                }

            /*if(!desperte && !mutex.hasWaiters(espera_temporales)){
                for (int i = 0; i < RedDePetri.cantT; i++) {
                    if(RedDePetri.getTiempoDeTransicion(i) != 0){
                        System.out.println("desperte pq no habia nadie " + i);
                        colas[i].signalAll();
                    }
                }
            }*/

            /*for (int i = 0; i < 4; i++) {
                if(invariantes[i]){
                    System.out.println(i);
                    colas[i].signalAll(); //despertar a todos los hilos de los invariantes que puedan ser ejectuados
                }
            }*/

        }else {
            for (int i = 0; i < Main.getCantT(); i++) {
                colas[i].signalAll();
            }
        }
    }
}
