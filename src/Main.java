public class Main {

    private static int cantP = 18;  //Cantidad de plazas de la red
    private static int cantT = 12;  //Cantidad de transiciones de la red
    private static int cantHilos =12;
    private static float porcentajeDePrioridadNoTemporales = (float) 0.01;
    private static Datos datos;
    private static RdP RedDePetri;
    private static InvariantesT inv;
    private static Politica politica;
    private static Monitor monitor;
    private static ThreadLinea[] hilos;
    private static Log logger;

    public static int getCantT() {
        return cantT;
    }
    public static float getPorcentajeDePrioridadNoTemporales() {return porcentajeDePrioridadNoTemporales;}

    public static void main(String[] args) {

        datos=new Datos();

        RedDePetri=new RdP(datos.crearMarcado("marcado.xls",cantP), //Marcado inicial de la red
                                datos.crearMatriz("matrizW.xls",cantT,cantP), //Matriz de incidencia conmbinada
                                datos.crearMatriz("matrizB.xls",cantT,cantP), //Matriz de incidencia negativa
                                datos.crearMatriz("pruebaInvP.xls",19,10), //Invariantes de plaza
                                datos.crearMatriz("transicionesPorInv.xls",3,4), //Invariantes de transicion
                                datos.crearMarcado("tiempoTransiciones.xls",cantT),
                                cantT,cantP);

        inv = new InvariantesT();

        politica= new Politica(inv,RedDePetri);

        monitor = new Monitor(RedDePetri, politica);

        hilos = new ThreadLinea[cantHilos];

        logger = new Log();

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

        for (int i = 0; i < cantHilos; i++) {
            hilos[i].start();
        }


        while(monitor.getContador()<1000){
            try {

                Thread.sleep(20);
                inv.printCantInvTCompletos();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

        inv.printCantInvTCompletos(logger);
        inv.printCantInvTCompletos();
        System.out.println(inv.getTransiciones());
        logger.logTransiciones(inv.getTransiciones());

    }
}
