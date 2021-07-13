public class InvariantesT {
    private String[] invT= {"","","",""};

    public InvariantesT() {
    }

    public void conteoTransiciones(int transicion,int linea){

        invT[linea] += "T"+transicion;
    }

    public String[] getInvT() {
        return invT;
    }

    public void printInvT(){
        for (int i = 0; i < 4; i++) {
            System.out.println(invT[i]);
        }
    }
}
