public class ThreadLinea1 extends ThreadLinea {

    private int linea;
    public ThreadLinea1(Monitor monitor, RdP red,InvariantesT inv, int linea) {

        super(monitor,red,inv);
        this.linea=linea;

    }

    @Override
    public void run() {

        while(!terminar){

            dispararTransicion(0,linea);  //TRANSICION 1
            inv.logTransicion(1,linea);

            dispararTransicion(7,linea);  //TRANSICION 2
            inv.logTransicion(2,linea);

            dispararTransicion(8,linea);  //TRANSICION 3
            inv.logTransicion(3,linea);

            inv.incInv(linea);
            monitor.incContador();

        }
    }
}
