public class ThreadLinea extends Thread{

    protected Monitor monitor;
    protected RdP red;
    protected InvariantesT inv;

    //private boolean terminar;

    public ThreadLinea(Monitor monitor, RdP red,InvariantesT inv) {
        this.monitor = monitor;
        this.red=red;
        this.inv=inv;
        //terminar=false;
    }

    public void dispararTransicion(int transicion) throws InterruptedException{
        monitor.disparar(transicion);
    }

}
