
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class Datos {

    public Datos() {}

    public int[] crearMarcado(String nombreArchivo, int cant_p){

        int[] vector = new int[cant_p];

        try (FileInputStream file = new FileInputStream(new File(nombreArchivo))) {
            HSSFWorkbook archivo = new HSSFWorkbook(file);
            HSSFSheet hoja = archivo.getSheetAt(0);
            Iterator<Row> fila_iterador = hoja.iterator();
            Row fila = fila_iterador.next();
            Iterator<Cell> columna_iterador = fila.cellIterator();
            Cell columna;
            int j = 0;
            while(columna_iterador.hasNext()){
                columna = columna_iterador.next();
                vector[j] = (int) columna.getNumericCellValue();
                j++;
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return vector;
    }

    public int[][] crearMatriz(String nombreArchivo, int cant_t , int cant_p  ) {

        int[][] matriz = new int[cant_p][cant_t];

        try (FileInputStream file = new FileInputStream(new File(nombreArchivo))) {
            HSSFWorkbook archivo = new HSSFWorkbook(file);
            HSSFSheet hoja = archivo.getSheetAt(0);
            Iterator<Row> fila_iterador = hoja.iterator();
            Row fila;
            int i = 0;
            while(i<cant_p){
                fila = fila_iterador.next();
                Iterator<Cell> columna_iterador = fila.cellIterator();
                Cell columna;
                int j = 0;
                while(j<cant_t){
                    columna = columna_iterador.next();
                    matriz[i][j] = (int) columna.getNumericCellValue();
                    j ++;
                }
                i++;
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return matriz;
    }
}
