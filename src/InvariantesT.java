public class InvariantesT {
    private String[] invT= {"","","",""};
    private String invariantesT;
    private int[] disparosInv= {0,0,0,0};


    public InvariantesT() {
        invariantesT=new String();
    }

    public void conteoTransiciones(int transicion,int linea){

        invT[linea] += "T"+transicion;
    }

    public synchronized void logTransicion(int transicion,int linea){
        invariantesT+="T"+transicion;
        disparosInv[linea]++;
    }

    public String[] getInvT() {
        return invT;
    }

    public String getInvariantesT() {
        return invariantesT;
    }

    public void printInvT(){
        for (int i = 0; i < 4; i++) {
            System.out.println(invT[i]);
        }
    }

    public void printCantDisparosInvT(){
        for (int i = 0; i < 4; i++) {
            System.out.println(disparosInv[i]);
        }
    }

}
