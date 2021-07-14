public class Politica {

    private InvariantesT inv;
    private RdP RedDePetri;
    private boolean finalizo;

    public Politica(InvariantesT inv, RdP RedDePetri) {

        this.inv = inv;
        this.RedDePetri=RedDePetri;
        finalizo=false;
    }

    public boolean[] determinarInv() {

        int[] transicionesInv = inv.getTransicionesInv();
        int inv = 0;

        for (int i = 0; i < 4; i++) {
            if (transicionesInv[i] >= transicionesInv[inv]) {
                inv = i;
            }
        }

        int cont=0;

        for (int i = 0; i < 4; i++) {
            if(i!=inv){
                for (int j = 0; j < 3; j++) {
                    if(RedDePetri.isHabilitada(RedDePetri.getTransicionesPorInv()[i][j])){
                        cont++;
                    }
                }
            }
        }

        boolean[] invariantes = {true,true,true,true};
        if(cont==0){ // solo se puede hacer el inv con mas transiciones
            for (int i = 0; i < 4; i++) {
                if(i==inv){
                    invariantes[i]=true;
                }else{
                    invariantes[i]=false;
                }
            }
        }else{
            //hace todas menos inv
            for (int i = 0; i < 4; i++) {
                if(i!=inv){
                    invariantes[i]=true;
                }else{
                    invariantes[i]=false;
                }
            }
        }

       return invariantes;
    }
}
