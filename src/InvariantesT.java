public class InvariantesT { //Se encarga de la informacion de transciones disparadas e invariantes completados

    private String transiciones; //Linea con las transiciones ejecutadas
    private int[] invCompletos= {0,0,0,0}; //Almacena la cant de invariantes completos de cada uno
    private int[] transicionesInv= {0,0,0,0}; //Almacena la cant de transiciones disparadas en cada invariante
    private int[] invTr= new int[]{0, 2, 2, 2, 3, 3, 3, 0, 0, 1, 1, 1};

    public InvariantesT() {
        transiciones =new String();
    }

    public synchronized void logTransicion(int transicion,int linea){
        transiciones +="T"+transicion+"-";
    }

    public String getTransiciones() {return transiciones;}

    public void printCantInvTCompletos(Log log){
        log.logInvariantes("------------------------------------------------------------------------------");


        for (int i = 0; i < 4; i++) {
            log.logInvariantes("Cantidad de invariantes completos en linea " + (i+1) + ":" + invCompletos[i]);
        }
    }

    public void printCantInvTCompletos(){
        System.out.println("------------------------------------------------------------------------------");
        for (int i = 0; i < 4; i++) {
            System.out.println("Cantidad de invariantes completos en linea " + (i+1) + " :" + invCompletos[i]);
        }
    }

    public synchronized void incInv(int linea){
        invCompletos[linea]++;
    }
    public synchronized void incTransicion(int linea){
        transicionesInv[linea]++;
    }

    public int getCantInvCompletosTr(int transicion){ return invCompletos[invTr[transicion]];}

    /*public int[] getMenorInvCompletos(){

        int [] invOrdenados = new int[invCompletos.length];

        for (int i = 0; i < invCompletos.length; i++) {
            if(invCompletos[i] > invCompletos[i+1]{

            }
        }
    }*/



}
