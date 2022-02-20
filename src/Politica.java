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

    public ArrayList<Integer> determinarTr() { //Determina la transicion que puede ejecutarse


        ArrayList<Integer> trDisparables = new ArrayList<>();//ArrayList de transiciones disparables ordenados de mayor a menor prioridad

        for (int i = 0; i < Main.getCantT(); i++) { //Selecciono la transicion a analizar
            if((RedDePetri.isHabilitada(i) || RedDePetri.getTiempoDeSensibilizacion(i) != 0) && conflicto(i)){ // Solo si esta habilitada
                for (int j = 0; j < Main.getCantT(); j++) {
                    if(j==trDisparables.size()){
                        trDisparables.add(j,i);
                        break;
                    }else {
                        if(inv.getCantInvCompletosTr(i) < inv.getCantInvCompletosTr(j)){
                            trDisparables.add(j,i);
                            break;
                        }
                    }
                }
            }
        }

        return trDisparables;
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
                    if(inv.getCantInvCompletosTr(trConflictiva) > inv.getCantInvCompletosTr(transicion)*(1+Main.getPorcentajeDePrioridadNoTemporales())){
                        habilitar=true;
                    }else{
                        habilitar= false;
                    }
                }
            }
        }
        return habilitar;
    }
}
