import java.io.FileWriter;
import java.io.PrintWriter;

public class Log {

    private FileWriter file;
    private PrintWriter pw;

    private final String LOG_DIR = "./data/log.txt";
    private final String LOG_I_DIR = "./data/log_inv.txt";

    public Log() {
        file = null;
        pw = null;
    }

    public void logTransiciones(String transiciones) {
        try {
            file = new FileWriter(LOG_DIR);
            pw = new PrintWriter(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pw.println(transiciones);
        pw.close();
    }

    public void logInvariantes(String invariantes){
        try{
            file = new FileWriter(LOG_I_DIR, true);
            pw = new PrintWriter(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        pw.println(invariantes);
        pw.close();
    }

}
