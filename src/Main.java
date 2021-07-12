public class Main {

    private static int cantP = 18;
    private static int cantT = 12;

    public static void main(String[] args) {

        Datos datos=new Datos();

        RdP RedDePetri=new RdP(datos.crearMarcado("marcado.xls",cantP),
                                datos.crearMatriz("matrizW.xls",cantT,cantP),
                                datos.crearMatriz("matrizB.xls",cantT,cantP),
                                cantT,cantP);


        for (int i = 0; i < cantP; i++) {
            System.out.print(RedDePetri.getMarcado()[i]);
        }
        System.out.print("\n");
        for (int i = 0; i < cantT; i++) {
            System.out.print(RedDePetri.getHabilitadas()[i]);
        }

        System.out.print("\n");
        RedDePetri.disparo(0);
        RedDePetri.disparo(0);

        for (int i = 0; i < cantP; i++) {
            System.out.print(RedDePetri.getMarcado()[i]);
        }
        System.out.print("\n");
        for (int i = 0; i < cantT; i++) {
            System.out.print(RedDePetri.getHabilitadas()[i]);
        }

    }
}
