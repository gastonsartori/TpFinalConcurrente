import static java.lang.Thread.currentThread;

public class ThreadLinea3 extends Thread {

    private Monitor monitor;
    private RdP red;
    private InvariantesT inv;
    private int linea;

    public ThreadLinea3(Monitor monitor,RdP red,InvariantesT inv,int linea) {
        this.monitor = monitor;
        this.red=red;
        this.inv=inv;
        this.linea=linea;
    }

    @Override
    public void run() {

        while(!isInterrupted()){

            dispararTransicion(1);  //TRANSICION 13
            inv.conteoTransiciones(13,linea);

            dispararTransicion(2);  //TRANSICION 14
            inv.conteoTransiciones(14,linea);

            dispararTransicion(3);  //TRANSICION 15
            inv.conteoTransiciones(15,linea);

        }

    }

    public void dispararTransicion(int transicion){
        monitor.disparar(transicion);
    }
}
