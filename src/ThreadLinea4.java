import static java.lang.Thread.currentThread;

public class ThreadLinea4 implements Runnable{

    private Monitor monitor;
    private RdP red;
    private InvariantesT inv;
    private int linea;

    public ThreadLinea4(Monitor monitor,RdP red,InvariantesT inv,int linea) {
        this.monitor = monitor;
        this.red=red;
        this.inv=inv;
        this.linea=linea;
    }

    @Override
    public void run() {

        //while(!(currentThread().isInterrupted())){

            dispararTransicion(4);  //TRANSICION 16
            inv.conteoTransiciones(16,linea);

            dispararTransicion(5);  //TRANSICION 17
            inv.conteoTransiciones(17,linea);

            dispararTransicion(6);  //TRANSICION 18
            inv.conteoTransiciones(18,linea);

        //}

    }

    public void dispararTransicion(int transicion){
        monitor.disparar(transicion);
    }
}
