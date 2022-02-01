public class ThreadLinea4 extends ThreadLinea {

    private int linea;


    public ThreadLinea4(Monitor monitor,RdP red,InvariantesT inv,int linea) {

        super(monitor,red,inv);
        this.linea=linea;
    }

    @Override
    public void run() {

        while(!terminar){

            dispararTransicion(4,linea);  //TRANSICION 16
            inv.logTransicion(16,linea);
            //System.out.println("T16");

            dispararTransicion(5,linea);  //TRANSICION 17
            inv.logTransicion(17,linea);
            //System.out.println("T17");

            dispararTransicion(6,linea);  //TRANSICION 18
            inv.logTransicion(18,linea);
            //System.out.println("T18");

            inv.incInv(linea);

            monitor.incContador();

        }
    }
}
