public class RdP {

    int cantP, cantT;
    private int[]   marcado; //Almacena el estado actual de la red en todo momento
    private int[][] matrizW; //Topologia de la red - incidencia combinada
    private int[][] matrizB; //Topologia de la red - incidencia negativa
    private int[][] matrizPinv; //Invariantes de plaza
    private int[][] transicionesPorInv; //Invariantes de transicion
    private long[]  tiempoDeSensibilizacion; //Tiempo en que se sensibilizo cada transicion temporal
    private int[]  transicionesTemporales; //Determina que transiciones son temporales, la que si lo son, almacenan el tiempo asignado

    private boolean[]   habilitadas; //Almacena las transiciones que se encuentran habilitadas en ese momento

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

    /**
     * Determina cuales transiciones estan habilitadas
     */
    public void habilitacion() {
        for (int i = 0; i < cantT; i++) {
            for (int j = 0; j < cantP; j++) {
                //chequeo de tokens
                if (marcado[j] < matrizB[j][i] && matrizB[j][i]!=0) { // Recorro la matriz W-, si hay conexion de una P a una T, esa plaza
                    habilitadas[i] = false;                           // debe tener tokens, sino la T no esta habilitada
                    if(esTemporal(i)){                                // En caso de transicion temporal, se resetea el tiempo
                        tiempoDeSensibilizacion[i]=0;
                    }
                    break;
                }else{
                    habilitadas[i]=true; //Si tiene los tokens, esta habilitada

                }
            }
            if(esTemporal(i) && habilitadas[i]){    //Si la transicion es temporal y tiene los tokens necesarios
                if(tiempoDeSensibilizacion[i]==0){  //Si el tiempo aun no esta contando
                    tiempoDeSensibilizacion[i] = System.currentTimeMillis(); //Se setea el inicio del conteo
                    habilitadas[i]=false;           //No esta habilitada pq recien empieza a contar
                }
                else{                               //Si el tiempo ya estaba contando
                    if(!tiempoFinalizado(i)){       //Se determina si transcurrio el tiempo necesario
                        habilitadas[i]=false;       //Si no lo hizo, no esta habilitada
                    }
                }
            }
        }
    }

    /**
     * Suma elemento a elemento, el marcado con la columna de la matriz de incidencia determinada por la transicion a disparar
     * se calcula nuevamente las transiciones habilitadas
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


    public long getTiempoDeSensibilizacion(int transicion) {
        return tiempoDeSensibilizacion[transicion];
    }


    public long getTiempoDeTransicion(int transicion) {
        return transicionesTemporales[transicion];
    }


    /**
     * Luego de cada transicion, se chequean los inv de plazas.
     * recorre cada inv de plazas, acumula los tokens de las plazas que pertenecen a cada invariante
     * al finalizar, esto debe ser igual a lo determinado por la matriz de chequeo
     * @return
     */

    public boolean chequeoInvP(){
        boolean error= false;
        for (int i = 0; i < matrizPinv.length; i++) { //Recorro cada fila de la matriz de chequeo
            int tokens=0;
            for (int j = 0; j < matrizPinv[i].length-1; j++) { //Recorro cada elemento de la fila
                if(matrizPinv[i][j] == 1){ //Si hay un 1, esa plaza pertnece a ese invariante
                    tokens+=marcado[j]; //Se acumulan los tokens de esa plaza
                }
            }
            if(tokens!=matrizPinv[i][matrizPinv[i].length-1]){ //Al final, la acumulacion debe ser igual al invariante
                error = true;
                break;
            }
        }
        return error;
    }


    public boolean esTemporal(int transicion){
        return transicionesTemporales[transicion] != 0;
    }


    public boolean tiempoFinalizado(int transicion){

        long tiempoActual = System.currentTimeMillis();
        long intervalo = tiempoActual - tiempoDeSensibilizacion[transicion];

        if(transicionesTemporales[transicion] < intervalo ) {	// Si se cumplio el tiempo para ser habilitada
            return true;
        }
        return false;
    }
}
