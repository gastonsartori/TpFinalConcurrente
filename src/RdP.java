
public class RdP {

    int cantP, cantT;
    private int[]   marcado; //almacenara el estado actual de la red en to do momento
    private int[][] matrizW; //topologia de la red - incidencia combinada
    private int[][] matrizB; //topologia de la red - incidencia negativa
    private int[][] matrizPinv; //invariantes de plaza
    private int[][] transicionesPorInv; //invariantes de transicion

    private boolean[]   habilitadas; //almacena que transiciones se encuentran habilitadas en ese momento

    public RdP(int[] marcado, int[][] matrizW, int[][] matrizB, int[][] matrizPinv, int[][] transicionesPorInv, int cantT, int cantP) {
        this.marcado = marcado;
        this.matrizW = matrizW;
        this.matrizB = matrizB;
        this.matrizPinv = matrizPinv;
        this.transicionesPorInv = transicionesPorInv;

        this.habilitadas = new boolean[cantT];
        this.cantT=cantT;
        this.cantP=cantP;

        habilitacion();

    }

    /**
     *
     * @param transicion
     * @return estado de habilitacion de la transicion
     */

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

    /**
     * suma elemento a elemento, el marcado con la columna de la matriz de incidencia determinada por la transicion a disparar
     * se calcula nuevament las transiciones habilitadas
     * se chequean los invariantes de plazas
     * @param transicion
     */
    public void disparo(int transicion){
        for (int i = 0; i < cantP; i++) {
            marcado[i]+=matrizW[i][transicion];
        }
        habilitacion();
        if(chequeoInvP()){
           System.out.println("ERROR");
        }
    }

    public int getCantP() { return cantP; }

    public int getCantT() { return cantT; }

    public int[] getMarcado() { return marcado; }

    public int[][] getMatrizW() { return matrizW; }

    public int[][] getMatrizB() { return matrizB; }

    public int[][] getMatrizPinv() { return matrizPinv; }

    public boolean[] getHabilitadas() { return habilitadas; }

    /**
     * luego de cada transicion, se chequean los inv de plazas.
     * recorre cada inv de plazas, acumula los tokens de las plazas que pertenecen a cada invariante
     * al finalizar, esto debe ser igual a lo determinado por la matriz de chequeo
     * @return
     */

    public boolean chequeoInvP(){
        boolean error= false;
        for (int i = 0; i < 10; i++) { //recorro cada fila de la matriz de chequeo
            int tokens=0;
            for (int j = 0; j < 18; j++) { //recorro cada elemento de la fila
                if(matrizPinv[i][j] == 1){ //si hay un 1, esa plaza pertnece a ese invariante
                    tokens+=marcado[j]; //se acumulan los tokens de esa plaza
                }
            }
            if(tokens!=matrizPinv[i][18]){ //al final, la acumulacion debe ser igual al invariante
                error = true;
                break;
            }
        }
        return error;
    }

    public int[][] getTransicionesPorInv() {
        return transicionesPorInv;
    }
}
