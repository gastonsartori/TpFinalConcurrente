
public class RdP {

    int cantP, cantT;
    private int[]   marcado;
    private int[][] matrizW;
    private int[][] matrizB;
    private int[][] matrizPinv;

    private boolean[]   habilitadas;

    public RdP(int[] marcado, int[][] matrizW, int[][] matrizB, int[][] matrizPinv, int cantT, int cantP) {
        this.marcado = marcado;
        this.matrizW = matrizW;
        this.matrizB = matrizB;
        this.matrizPinv = matrizPinv;

        this.habilitadas = new boolean[cantT];
        this.cantT=cantT;
        this.cantP=cantP;

        habilitacion();

    }

    public boolean isHabilitada(int transicion) {
        return habilitadas[transicion];
    }

    public void printMarcado(){
        for (int i = 0; i < cantT; i++) {
            System.out.print(marcado[i]);
        }
        System.out.print("\n");
    }

    public void habilitacion() { // metodo para determinar que transcionciones estan habilitadas
        for (int i = 0; i < cantT; i++) {
            for (int j = 0; j < cantP; j++) {
                if (marcado[j] < matrizB[j][i]) { // recorro la matriz W-, si hay conexion de una P a una T, esa plaza
                    habilitadas[i] = false;       // debe tener tokens, sino la T no esta habilitada
                    break;
                }
                habilitadas[i]=true;
            }
        }
    }

    public void disparo(int transicion){
        for (int i = 0; i < cantP; i++) {
            marcado[i]+=matrizW[i][transicion];
        }
        habilitacion();
        if(chequeoInvP()){
           System.out.println("ERROR");
        }else{
            System.out.println("NO ERROR");
        }

    }

    public int getCantP() { return cantP; }

    public int getCantT() { return cantT; }

    public int[] getMarcado() { return marcado; }

    public int[][] getMatrizW() { return matrizW; }

    public int[][] getMatrizB() { return matrizB; }

    public int[][] getMatrizPinv() { return matrizPinv; }

    public boolean[] getHabilitadas() { return habilitadas; }

    public boolean chequeoInvP(){
        boolean error= false;
        for (int i = 0; i < 10; i++) {
            int tokens=0;
            for (int j = 0; j < 18; j++) {
                if(matrizPinv[i][j] == 1){
                    tokens+=marcado[j];
                }
            }
            if(tokens!=matrizPinv[i][18]){
                error = true;
                break;
            }
        }
        return error;
    }
}
