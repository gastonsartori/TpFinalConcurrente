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

        while(!monitor.disparar(transicion)){
            long tiempoSleep =red.getTiempoDeSleep(transicion);

            if (tiempoSleep > 0) {
                try {
                    //System.out.println("Duermo: " + System.currentTimeMillis());
                    sleep(tiempoSleep);
                    //System.out.println("Despierto: " + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        inv.incTransicion(linea);

    }

    public void fin(){
        terminar=true;
    }

}
