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
            //System.out.println("T4");

            dispararTransicion(10,linea);  //TRANSICION 5
            inv.logTransicion(5,linea);
            //System.out.println("T5");

            dispararTransicion(11,linea);  //TRANSICION 6
            inv.logTransicion(6,linea);
            //System.out.println("T6");

            inv.incInv(linea);
            monitor.incContador();

        }

    }

}
