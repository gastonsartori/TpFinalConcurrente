import static java.lang.Thread.currentThread;

public class ThreadLinea4 extends ThreadLinea {

    private int linea;


    public ThreadLinea4(Monitor monitor,RdP red,InvariantesT inv,int linea) {

        super(monitor,red,inv);
        this.linea=linea;
    }

    @Override
    public void run() {

        while(!(currentThread().isInterrupted())){

            try {
                dispararTransicion(4);  //TRANSICION 16
                inv.conteoTransiciones(16, linea);
                inv.logTransicion(16,linea);

                dispararTransicion(5);  //TRANSICION 17
                inv.conteoTransiciones(17, linea);
                inv.logTransicion(17,linea);

                dispararTransicion(6);  //TRANSICION 18
                inv.conteoTransiciones(18, linea);
                inv.logTransicion(18,linea);

                monitor.incContador();

            }catch(InterruptedException e){
                return;
            }
        }
    }
}
