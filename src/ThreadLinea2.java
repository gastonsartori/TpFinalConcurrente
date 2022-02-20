public class ThreadLinea2 extends ThreadLinea {

    private int linea;

    public ThreadLinea2(Monitor monitor,RdP red,InvariantesT inv,int linea) {

        super(monitor,red,inv);
        this.linea=linea;


    }

    @Override
    public void run() {

        while(!terminar){

            dispararTransicion(9,linea);  //TRANSICION 4
            inv.logTransicion(4,linea);

            dispararTransicion(10,linea);  //TRANSICION 5
            inv.logTransicion(5,linea);

            dispararTransicion(11,linea);  //TRANSICION 6
            inv.logTransicion(6,linea);

            inv.incInv(linea);
            monitor.incContador();

        }

    }

}
