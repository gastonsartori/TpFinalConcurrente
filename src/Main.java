public class Main {

    private static int cantP = 18;
    private static int cantT = 12;

    public static void main(String[] args) {

        Datos datos=new Datos();

        RdP RedDePetri=new RdP(datos.crearMarcado("marcado.xls",cantP),
                                datos.crearMatriz("matrizW.xls",cantT,cantP),
                                datos.crearMatriz("matrizB.xls",cantT,cantP),
                                datos.crearMatriz("pruebaInvP.xls",19,10),
                                cantT,cantP);
        /*
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 19; j++) {
                System.out.print(RedDePetri.getMatrizPinv()[i][j]);
            }
            System.out.println("\n");
        }

         */


        Monitor monitor = new Monitor(RedDePetri);

        InvariantesT inv = new InvariantesT();

        ThreadLinea[] hilos = new ThreadLinea[12];

        for (int i = 0; i < 3; i++) {
            hilos[i]= new ThreadLinea1(monitor,RedDePetri,inv,0);
            hilos[i].setName("linea1");
        }
        for (int i = 3; i < 6; i++) {
            hilos[i]= new ThreadLinea2(monitor,RedDePetri,inv,1);
            hilos[i].setName("linea2");
        }
        for (int i = 6; i < 9; i++) {
            hilos[i]= new ThreadLinea3(monitor,RedDePetri,inv,2);
            hilos[i].setName("linea3");
        }
        for (int i = 9; i < 12; i++) {
            hilos[i]= new ThreadLinea4(monitor,RedDePetri,inv,3);
            hilos[i].setName("linea4");
        }


        //Thread hilo2 = new Thread(new ThreadLinea2(monitor,RedDePetri,inv,1));
        //Thread hilo3 = new Thread(new ThreadLinea3(monitor,RedDePetri,inv,2));
        //Thread hilo4 = new Thread(new ThreadLinea4(monitor,RedDePetri,inv,3));

        for (int i = 0; i < 12; i++) {
            hilos[i].start();
        }
        //hilo1.start();
        //hilo2.start();
        // hilo3.start();
        //hilo4.start();

        while(monitor.getContador()<1000){
            System.out.print("");
        }

        //hilo1.fin();
        //hilo2.fin();
        //hilo3.fin();
        //hilo4.fin();

        for (int i = 0; i < 12; i++) {
            hilos[i].fin();
        }

        //hilo1.interrupt();
        //hilo2.interrupt();
        //hilo3.interrupt();
        //hilo4.interrupt();

        inv.printInvT();
        inv.printCantDisparosInvT();
        //System.out.println(inv.getInvariantesT());

    }
}
