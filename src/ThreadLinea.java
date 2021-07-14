public class ThreadLinea extends Thread{

    protected Monitor monitor;
    protected RdP red;
    protected InvariantesT inv;
    protected boolean terminar;

    public ThreadLinea(Monitor monitor, RdP red,InvariantesT inv) {
        this.monitor = monitor;
        this.red=red;
        this.inv=inv;
        terminar=false;
    }

    public void dispararTransicion(int transicion,int linea){
        monitor.disparar(transicion);
        inv.incTransicion(linea);

    }

    public void fin(){
        terminar=true;
    }

}
