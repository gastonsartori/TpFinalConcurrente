public class InvariantesT { //se encarga de la informacion de transciones disparadas y invariantes completados

    private String transiciones; //linea con las transiciones ejecutadas
    private int[] invCompletos= {0,0,0,0}; //almacena la cant de invariantes completos de cada uno
    private int[] transicionesInv= {0,0,0,0}; //almacena la cant de transiciones disparadas en cada invariante

    public InvariantesT() {
        transiciones =new String();
    }

    public synchronized void logTransicion(int transicion,int linea){
        transiciones +="T"+transicion+"-";
    }

    public String getTransiciones() {
        return transiciones;
    }

    public void printCantInvTCompletos(){
        for (int i = 0; i < 4; i++) {
            System.out.println("Cantidad de invariantes completos en linea " + (i+1) + " :" + invCompletos[i]);
        }
    }

    public void printCantTransicionesInv(){
        for (int i = 0; i < 4; i++) {
            System.out.println("Cantidad de transiciones disparadas en linea " + (i+1) + " :" + transicionesInv[i]);
        }
    }

    //TODO: MEJORAR SINCRONIZACION DE LOS METODOS

    public synchronized void incInv(int linea){
        invCompletos[linea]++;
    }

    public synchronized void incTransicion(int linea){
        transicionesInv[linea]++;
    }

    public int[] getInvCompletos() {
        return invCompletos;
    }

    public int[] getTransicionesInv() {
        return transicionesInv;
    }


}
