import static java.lang.Thread.currentThread;

public class ThreadLinea2 extends ThreadLinea {

    private int linea;

    public ThreadLinea2(Monitor monitor,RdP red,InvariantesT inv,int linea) {

        super(monitor,red,inv);
        this.linea=linea;


    }

    @Override
    public void run() {

        while(!(currentThread().isInterrupted())){

            try {
                dispararTransicion(9);  //TRANSICION 4
                inv.conteoTransiciones(4, linea);
                inv.logTransicion(4,linea);

                dispararTransicion(10);  //TRANSICION 5
                inv.conteoTransiciones(5, linea);
                inv.logTransicion(5,linea);

                dispararTransicion(11);  //TRANSICION 6
                inv.conteoTransiciones(6, linea);
                inv.logTransicion(6,linea);

                monitor.incContador();

            }catch(InterruptedException e){
                return;
            }

        }

    }

}
