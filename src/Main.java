public class Main {

    private static int cantP = 18;  //cantidad de plazas de la red
    private static int cantT = 12;  //cantidad de transiciones de la red

    public static void main(String[] args) {

        Datos datos=new Datos();

        RdP RedDePetri=new RdP(datos.crearMarcado("marcado.xls",cantP), //marcad inicial de la red
                                datos.crearMatriz("matrizW.xls",cantT,cantP), //matriz de incidencia conmbinada
                                datos.crearMatriz("matrizB.xls",cantT,cantP), //matriz de incidencia negativa
                                datos.crearMatriz("pruebaInvP.xls",19,10), //invariantes de plaza
                                datos.crearMatriz("transicionesPorInv.xls",3,4), //invariantes de transicion
                                cantT,cantP);

        /*
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(RedDePetri.getTransicionesPorInv()[i][j]);
            }
            System.out.println("\n");
        }
        */


        InvariantesT inv = new InvariantesT();

        Politica politica= new Politica(inv,RedDePetri);

        Monitor monitor = new Monitor(RedDePetri, politica);

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

        monitor.finalizar();

        for (int i = 0; i < 12; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //hilo1.interrupt();
        //hilo2.interrupt();
        //hilo3.interrupt();
        //hilo4.interrupt();

        inv.printCantInvTCompletos();
        //inv.printCantTransicionesInv();
        System.out.println(inv.getTransiciones());

        Log logger = new Log();

        logger.logTransiciones(inv.getTransiciones());


    }
}
