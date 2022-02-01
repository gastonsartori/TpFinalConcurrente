public class ThreadLinea3 extends ThreadLinea {

    private int linea;

    public ThreadLinea3(Monitor monitor,RdP red,InvariantesT inv,int linea) {
        super(monitor,red,inv);
        this.linea=linea;
    }

    @Override
    public void run() {

        while(!terminar){

            dispararTransicion(1,linea);  //TRANSICION 13
            inv.logTransicion(13,linea);
            //System.out.println("T13");

            dispararTransicion(2,linea);  //TRANSICION 14
            inv.logTransicion(14,linea);
            //System.out.println("T14");

            dispararTransicion(3,linea);  //TRANSICION 15
            inv.logTransicion(15,linea);
            //System.out.println("T15");

            inv.incInv(linea);
            monitor.incContador();

        }
    }

}

