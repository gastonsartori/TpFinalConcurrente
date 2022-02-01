public class Main {

    private static int cantP = 18;  //cantidad de plazas de la red
    private static int cantT = 12;  //cantidad de transiciones de la red
    private static int cantHilos =12;

    public static int getCantP() {
        return cantP;
    }

    public static int getCantT() {
        return cantT;
    }

    public static int getCantHilos() {
        return cantHilos;
    }

    public static void main(String[] args) {

        Datos datos=new Datos();

        RdP RedDePetri=new RdP(datos.crearMarcado("marcado.xls",cantP), //marcad inicial de la red
                                datos.crearMatriz("matrizW.xls",cantT,cantP), //matriz de incidencia conmbinada
                                datos.crearMatriz("matrizB.xls",cantT,cantP), //matriz de incidencia negativa
                                datos.crearMatriz("pruebaInvP.xls",19,10), //invariantes de plaza
                                datos.crearMatriz("transicionesPorInv.xls",3,4), //invariantes de transicion
                                datos.crearMarcado("tiempoTransiciones.xls",cantT),
                                cantT,cantP);

        /*
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(RedDePetri.getTransicionesPorInv()[i][j]);
            }
            System.out.println("\n");
        }
        */

        RedDePetri.printMarcado();
        //RedDePetri.printTemporales();
        RedDePetri.printTiempos();
        //RedDePetri.printB();
        RedDePetri.printHabilitadas();

        InvariantesT inv = new InvariantesT();

        Politica politica= new Politica(inv,RedDePetri);

        Monitor monitor = new Monitor(RedDePetri, politica);

        ThreadLinea[] hilos = new ThreadLinea[cantHilos];

        for (int i = 0; i < cantHilos/4; i++) {
            hilos[i]= new ThreadLinea1(monitor,RedDePetri,inv,0);
            hilos[i].setName("linea1");
        }
        for (int i = cantHilos/4; i < cantHilos/2; i++) {
            hilos[i]= new ThreadLinea2(monitor,RedDePetri,inv,1);
            hilos[i].setName("linea2");
        }
        for (int i = cantHilos/2; i < cantHilos*3/4; i++) {
            hilos[i]= new ThreadLinea3(monitor,RedDePetri,inv,2);
            hilos[i].setName("linea3");
        }
        for (int i = cantHilos*3/4; i < cantHilos; i++) {
            hilos[i]= new ThreadLinea4(monitor,RedDePetri,inv,3);
            hilos[i].setName("linea4");
        }

        //Thread hilo2 = new Thread(new ThreadLinea2(monitor,RedDePetri,inv,1));
        //Thread hilo3 = new Thread(new ThreadLinea3(monitor,RedDePetri,inv,2));
        //Thread hilo4 = new Thread(new ThreadLinea4(monitor,RedDePetri,inv,3));

        long tiempoInicio=System.currentTimeMillis();

        for (int i = 0; i < cantHilos; i++) {
            hilos[i].start();
        }

        while(monitor.getContador()<1000){
            /*try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            inv.printCantInvTCompletos();*/
            //System.out.println("");
       }

        for (int i = 0; i < cantHilos; i++) {
            hilos[i].fin();
        }

        monitor.finalizar();

        for (int i = 0; i < cantHilos; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long tiempoFin=System.currentTimeMillis();

        long tiempoEjecucion=tiempoFin-tiempoInicio;

        System.out.println(tiempoEjecucion);

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
