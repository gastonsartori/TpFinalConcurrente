import java.io.FileWriter;
import java.io.PrintWriter;

public class Log {

    private FileWriter file;
    private PrintWriter pw;

    private String LOG_DIR = ".\\data\\log.txt";

    public Log(){
        file=null;
        pw=null;
    }

    public void logTransiciones(String transiciones){
        try{
            file = new FileWriter(LOG_DIR);
            pw = new PrintWriter(file);
        } catch (Exception e){
            e.printStackTrace();
        }

        pw.println(transiciones);
        pw.close();
    }

}
