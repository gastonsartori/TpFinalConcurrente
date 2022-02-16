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
        invTr= new int[]{0, 2, 2, 2, 3, 3, 3, 0, 0, 1, 1, 1};
        conflictos=new int[][]{{5,10},{5,10},{},{},{2},{},{},{},{},{7},{},{}};
    }

    public ArrayList<Integer> determinarTr() { //determina la transicion que puede ejecutarse

        int invMin = 10000;
        int trDespertar=0;

        ArrayList<Integer> trDisparables = new ArrayList<>();

        for (int i = 0; i < Main.getCantT(); i++) { //selecciono la transicion a analizar
            if((RedDePetri.isHabilitada(i) || RedDePetri.getTiempoDeSensibilizacion(i) != 0) && conflicto(i)){ // solo si esta habiliatada
                for (int j = 0; j < Main.getCantT(); j++) {
                    if(j==trDisparables.size()){
                        trDisparables.add(j,i);
                        break;
                    }else{
                        if(inv.getCantInvCompletosTr(i) < inv.getCantInvCompletosTr(j)){
                            trDisparables.add(j,i);
                            break;
                        }
                    }
                }
            }
        }

        /*for (int i = 0; i < trDisparables.size(); i++) {
            System.out.println("disparables");
            System.out.println(trDisparables.get(i));
            System.out.println("---");
        }*/

        /*
        for (int i = 0; i < Main.getCantT(); i++) {
            if(RedDePetri.isHabilitada(i)){
                if(inv.getCantInvCompletos(invTr[i]) < invMin){
                    invMin=inv.getCantInvCompletos(invTr[i]);
                    trDespertar=i;
                }
            }
        }

         */

        return trDisparables;
    }

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
