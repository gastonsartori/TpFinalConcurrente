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

        Thread hilo1 = new ThreadLinea1(monitor,RedDePetri,inv,0);
        Thread hilo2 = new ThreadLinea2(monitor,RedDePetri,inv,1);
        Thread hilo3 = new ThreadLinea3(monitor,RedDePetri,inv,2);
        Thread hilo4 = new ThreadLinea4(monitor,RedDePetri,inv,3);
        //Thread hilo2 = new Thread(new ThreadLinea2(monitor,RedDePetri,inv,1));
        //Thread hilo3 = new Thread(new ThreadLinea3(monitor,RedDePetri,inv,2));
        //Thread hilo4 = new Thread(new ThreadLinea4(monitor,RedDePetri,inv,3));

        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();

        while(monitor.getContador()<100){
            System.out.println("dentro de while");
        }
        System.out.println("sali de while");

        hilo1.interrupt();
        hilo2.interrupt();
        hilo3.interrupt();
        hilo4.interrupt();

        inv.printInvT();



    }
}
