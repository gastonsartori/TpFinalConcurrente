public class ThreadLinea1 extends ThreadLinea {

    private int linea;
    public ThreadLinea1(Monitor monitor, RdP red,InvariantesT inv, int linea) {

        super(monitor,red,inv);
        this.linea=linea;

    }

    @Override
    public void run() {

        while(!terminar){ //Se chequea la bandera de finalizacion antes de comnezar un nuevo invariante

            dispararTransicion(0,linea);  //TRANSICION 1
            inv.logTransicion(1,linea);   //Luego de disparar, la contabiliza
            //System.out.println("T1");

            dispararTransicion(7,linea);  //TRANSICION 2
            inv.logTransicion(2,linea);
            //System.out.println("T2");

            dispararTransicion(8,linea);  //TRANSICION 3
            inv.logTransicion(3,linea);
            //System.out.println("T3");

            inv.incInv(linea); //Al finalizar un inv, suma a los inv completados
            monitor.incContador();

        }
    }
}
