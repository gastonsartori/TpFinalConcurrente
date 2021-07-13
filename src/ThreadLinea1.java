import static java.lang.Thread.currentThread;

public class ThreadLinea1 implements Runnable{

    private Monitor monitor;
    private RdP red;
    private InvariantesT inv;
    private int linea;

    public ThreadLinea1(Monitor monitor, RdP red,InvariantesT inv, int linea) {
        this.monitor = monitor;
        this.red=red;
        this.inv=inv;
        this.linea=linea;
    }

    @Override
    public void run() {

        //while(!(currentThread().isInterrupted())){

            dispararTransicion(0);  //TRANSICION 1
            inv.conteoTransiciones(1,linea);

            dispararTransicion(7);  //TRANSICION 2
            inv.conteoTransiciones(2,linea);

            dispararTransicion(8);  //TRANSICION 3
            inv.conteoTransiciones(3,linea);

        //}

    }

    public void dispararTransicion(int transicion){
        monitor.disparar(transicion);
    }
}
