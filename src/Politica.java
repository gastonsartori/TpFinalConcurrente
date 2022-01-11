public class Politica {

    private InvariantesT inv;
    private RdP RedDePetri;
    private boolean finalizo;

    public Politica(InvariantesT inv, RdP RedDePetri) {

        this.inv = inv;
        this.RedDePetri=RedDePetri;
        finalizo=false;
    }

    public boolean[] determinarInv() { //determina el invariante que puede ejecutarse

        int[] transicionesInv = inv.getTransicionesInv(); //obtiene la informacion de los invariantes completados
        int inv = 0;

        for (int i = 0; i < 4; i++) {
            if (transicionesInv[i] >= transicionesInv[inv]) {
                inv = i; //se almacena el indice del invariante con mayor numero invaraintes completos
            }
        }

        int cont=0;

        for (int i = 0; i < 4; i++) {
            if(i!=inv){ //recoremos los invaraintes, menos el que mas tiene completos
                for (int j = 0; j < 3; j++) {
                    if(RedDePetri.isHabilitada(RedDePetri.getTransicionesPorInv()[i][j])){
                        cont++; //aumenta por cada transicion habilitada de los demas invaraintes
                    }
                }
            }
        }

        boolean[] invariantes = {true,true,true,true};
        if(cont==0){ // solo se enceuntran habilitadas transicions del inv con mas inv completos
            for (int i = 0; i < 4; i++) {
                if(i==inv){
                    invariantes[i]=true;  //solo este puede ser ejecutado
                }else{
                    invariantes[i]=false;
                }
            }
        }else{ //caso contraio
            for (int i = 0; i < 4; i++) {
                if(i!=inv){
                    invariantes[i]=true; //pueden ejecutarse alguno de los otros 3 inv
                }else{
                    invariantes[i]=false;
                }
            }
        }

       return invariantes;
    }
}
