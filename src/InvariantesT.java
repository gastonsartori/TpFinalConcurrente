public class InvariantesT {

    private String transiciones;
    private int[] invCompletos= {0,0,0,0};
    private int[] transicionesInv= {0,0,0,0};

    public InvariantesT() {
        transiciones =new String();
    }

    public synchronized void logTransicion(int transicion,int linea){
        transiciones +="T"+transicion;
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
