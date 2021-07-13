public class ThreadLinea1 extends ThreadLinea {

    private int linea;
    public ThreadLinea1(Monitor monitor, RdP red,InvariantesT inv, int linea) {

        super(monitor,red,inv);
        this.linea=linea;

    }

    @Override
    public void run() {

        while(!(currentThread().isInterrupted())){

            try{
                dispararTransicion(0);  //TRANSICION 1
                inv.conteoTransiciones(1,linea);
                inv.logTransicion(1,linea);

                dispararTransicion(7);  //TRANSICION 2
                inv.conteoTransiciones(2,linea);
                inv.logTransicion(2,linea);

                dispararTransicion(8);  //TRANSICION 3
                inv.conteoTransiciones(3,linea);
                inv.logTransicion(3,linea);

                monitor.incContador();

            }catch(InterruptedException e){
                return;
            }
        }
    }
}
