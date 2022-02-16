
public class RdP {

    int cantP, cantT;
    private int[]   marcado; //almacenara el estado actual de la red en to do momento
    private int[][] matrizW; //topologia de la red - incidencia combinada
    private int[][] matrizB; //topologia de la red - incidencia negativa
    private int[][] matrizPinv; //invariantes de plaza
    private int[][] transicionesPorInv; //invariantes de transicion
    private long[]  tiempoDeSensibilizacion; //tiempo en que se sensibilizo cada transicion temporal
    private int[]  transicionesTemporales; //determina que transiciones son temporales, la que si lo son, almacenan el tiempo asignado

    private boolean[]   habilitadas; //almacena que transiciones se encuentran habilitadas en ese momento

    public RdP(int[] marcado, int[][] matrizW, int[][] matrizB, int[][] matrizPinv, int[][] transicionesPorInv, int[] transicionesTemporales, int cantT, int cantP) {
        this.marcado = marcado;
        this.matrizW = matrizW;
        this.matrizB = matrizB;
        this.matrizPinv = matrizPinv;
        this.transicionesPorInv = transicionesPorInv;
        this.transicionesTemporales=transicionesTemporales;
        this.tiempoDeSensibilizacion=new long[cantT];

        for (int i = 0; i < cantT; i++) {
            tiempoDeSensibilizacion[i]=0;
        }

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
        for (int i = 0; i < cantP; i++) {
            System.out.print(marcado[i]);
        }
        System.out.print("\n");
    }

    public void printTemporales(){
        for (int i = 0; i < cantT; i++) {
            System.out.print(transicionesTemporales[i]);
        }
        System.out.print("\n");
    }

    public void printTiempos(){
        for (int i = 0; i < cantT; i++) {
            System.out.print(tiempoDeSensibilizacion[i]);
        }
        System.out.print("\n");
    }

    public void printHabilitadas(){
        for (int i = 0; i < cantT; i++) {
            System.out.print(habilitadas[i]);
        }
        System.out.print("\n");
    }
    
    public void printB(){
        for (int i = 0; i < cantP; i++) {
            for (int j = 0; j < cantT; j++) {
                System.out.print(matrizB[i][j]);
            }
            System.out.println("");
        }
    }

    public void habilitacion() { // metodo para determinar que transcionciones estan habilitadas
        for (int i = 0; i < cantT; i++) {
            for (int j = 0; j < cantP; j++) {
                //chequeo de tokens
                if (marcado[j] < matrizB[j][i] && matrizB[j][i]!=0) { // recorro la matriz W-, si hay conexion de una P a una T, esa plaza
                    //System.out.println("no tiene tokens");
                    habilitadas[i] = false;       // debe tener tokens, sino la T no esta habilitada
                    if(esTemporal(i)){  //en caso de transicion temporal, se resetea el tiempo
                        tiempoDeSensibilizacion[i]=0;
                    }
                    break;
                }else{
                    habilitadas[i]=true;
                }
                //si tiene los tokens necesarios
                //System.out.println("tiene tokens");
                    //si tiene los tokens, y en caso de ser temporal, transcurrio el tiempo, esta habilitada
            }

            if(esTemporal(i) && habilitadas[i]){ //si la transicion es temporal
                //System.out.println("t temp y habilitada");
                if(tiempoDeSensibilizacion[i]==0){  //si el tiempo aun no esta contando
                    tiempoDeSensibilizacion[i] = System.currentTimeMillis(); //se setea el inicio del conteo
                    habilitadas[i]=false; //no esta habilitada pq recien empieza a contar
                }else{  //si el tiempo ya estaba contando
                    if(!tiempoFinalizado(i)){       //se determina si transcurrio el tiempo necesario
                        habilitadas[i]=false;   //si no lo hizo, no esta habilitada
                    }
                }
            }

            //System.out.print(habilitadas[i]);

        }
        //System.out.println("");
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
           System.exit(1);
        }
    }

    public int getCantP() { return cantP; }

    public int getCantT() { return cantT; }

    public int[] getMarcado() { return marcado; }

    public int[][] getMatrizW() { return matrizW; }

    public int[][] getMatrizB() { return matrizB; }

    public int[][] getMatrizPinv() { return matrizPinv; }

    public boolean[] getHabilitadas() { return habilitadas; }

    public long getTiempoDeSensibilizacion(int transicion) {
        return tiempoDeSensibilizacion[transicion];
    }

    public int[] getTransicionesTemporales() {
        return transicionesTemporales;
    }

    public long getTiempoDeTransicion(int transicion) {
        return transicionesTemporales[transicion];
    }


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

    public boolean esTemporal(int transicion){
        return transicionesTemporales[transicion] != 0;
    }

    public long[] getTiempoDeSensibilizacion() {
        return tiempoDeSensibilizacion;
    }

    public boolean tiempoFinalizado(int transicion){

        long tiempoActual = System.currentTimeMillis();
        long intervalo = tiempoActual - tiempoDeSensibilizacion[transicion];

        //System.out.println(intervalo);

        if( transicionesTemporales[transicion] < intervalo ){	// Si se cumplio el tiempo para ser habilitada
            //System.out.println("finalizado");
            return true;
        }

        return false;
    }
}
