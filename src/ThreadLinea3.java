import static java.lang.Thread.currentThread;

public class ThreadLinea3 extends ThreadLinea {

    private int linea;

    public ThreadLinea3(Monitor monitor,RdP red,InvariantesT inv,int linea) {
        super(monitor,red,inv);
        this.linea=linea;
    }

    @Override
    public void run() {

        while(!(currentThread().isInterrupted())){
            try {
                dispararTransicion(1);  //TRANSICION 13
                inv.conteoTransiciones(13, linea);
                inv.logTransicion(13,linea);

                dispararTransicion(2);  //TRANSICION 14
                inv.conteoTransiciones(14, linea);
                inv.logTransicion(14,linea);

                dispararTransicion(3);  //TRANSICION 15
                inv.conteoTransiciones(15, linea);
                inv.logTransicion(15,linea);

                monitor.incContador();

            }catch(InterruptedException e){
                return;
            }
        }

    }

}
