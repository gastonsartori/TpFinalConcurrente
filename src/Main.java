public class Main {

    private static int cantP = 18;
    private static int cantT = 12;

    public static void main(String[] args) {

        Datos datos=new Datos();

        RdP RedDePetri=new RdP(datos.crearMarcado("marcado.xls",cantP),
                                datos.crearMatriz("matrizW.xls",cantT,cantP),
                                datos.crearMatriz("matrizB.xls",cantT,cantP),
                                cantT,cantP);

        Monitor monitor = new Monitor(RedDePetri);

        InvariantesT inv = new InvariantesT();

        Thread hilo1 = new Thread(new ThreadLinea1(monitor,RedDePetri,inv,0));
        Thread hilo2 = new Thread(new ThreadLinea2(monitor,RedDePetri,inv,1));
        Thread hilo3 = new Thread(new ThreadLinea3(monitor,RedDePetri,inv,2));
        Thread hilo4 = new Thread(new ThreadLinea4(monitor,RedDePetri,inv,3));

        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();

        try {
            hilo1.join();
            hilo2.join();
            hilo3.join();
            hilo4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inv.printInvT();


    }
}
