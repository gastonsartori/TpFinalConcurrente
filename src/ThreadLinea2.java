import static java.lang.Thread.currentThread;

public class ThreadLinea2 extends Thread {

    private Monitor monitor;
    private RdP red;
    private InvariantesT inv;
    private int linea;

    public ThreadLinea2(Monitor monitor,RdP red,InvariantesT inv,int linea) {
        this.monitor = monitor;
        this.red=red;
        this.inv=inv;
        this.linea=linea;
    }

    @Override
    public void run() {

        while(!isInterrupted()){

            dispararTransicion(9);  //TRANSICION 4
            inv.conteoTransiciones(4,linea);

            dispararTransicion(10);  //TRANSICION 5
            inv.conteoTransiciones(5,linea);

            dispararTransicion(11);  //TRANSICION 6
            inv.conteoTransiciones(6,linea);

        }

    }

    public void dispararTransicion(int transicion){
        monitor.disparar(transicion);
    }
}
