import java.util.ArrayList;

public class Politica {

    private InvariantesT inv;
    private RdP RedDePetri;
    private boolean finalizo;
    private int[] invTr;
    private int[][] conflictos;

    public Politica(InvariantesT inv, RdP RedDePetri) {

        this.inv = inv;
        this.RedDePetri=RedDePetri;
        finalizo=false;
        invTr= new int[]{0, 2, 2, 2, 3, 3, 3, 0, 0, 1, 1, 1};//Indica a que linea pertenece cada transicion
        conflictos=new int[][]{{5,10},{5,10},{},{},{2},{},{},{},{},{7},{},{}};//Indica cuales transiciones no temporales, tienen conflicto con transiciones temporales.
    }


    /**
     * Determina si las transiciones posibles de disparar tienen conflicto.
     * @param transiciones
     * @return
     */
    public boolean hayDisparable(boolean[] transiciones) {
        for (int i = 0; i < transiciones.length; i++) {
            if(transiciones[i] && conflicto(i)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determina como resolver el conflicto entre temporales y no temporales, dandole recursos o no, a la transicion no temporal
     * @param transicion
     * @return
     */
    private boolean conflicto(int transicion){

        boolean habilitar = true;

        if(!RedDePetri.esTemporal(transicion)){
            for (int i = 0; i < conflictos[transicion].length; i++) {
                int trConflictiva = conflictos[transicion][i];
                if(RedDePetri.getTiempoDeSensibilizacion(trConflictiva) != 0){
                    if(inv.getCantTransInvTr(trConflictiva) > inv.getCantTransInvTr(transicion)*(1+Main.getPorcentajeDePrioridadNoTemporales())){
                        habilitar=true;
                    }else{
                        habilitar= false;
                    }
                }
            }
        }
        return habilitar;
    }

    /**
     * Determinar cual transicion despertar, teniendo en cuenta la cantidad de invariantes completos de cada uno.
     * @param transiciones
     * @return
     */
    public int cualDespertar(boolean[] transiciones) { //Determina la transicion que puede ejecutarse
        int trADisparar = -1;

        for (int i = 0; i < transiciones.length; i++) { //Selecciono la transicion a analizar
            if(transiciones[i] && conflicto(i)){ //Si es disparable
                if(trADisparar == -1 || inv.getCantTransInvTr(i) < inv.getCantTransInvTr(trADisparar))
                    trADisparar = i;
            }
        }
        return trADisparar;
    }
}
