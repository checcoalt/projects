package server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class LoggerThread extends Thread {

    private int dato;

    public LoggerThread(int dato) {
        this.dato = dato;
    }

    public void run() {

        try {

            FileOutputStream out = new FileOutputStream("../log.txt", true);
            PrintWriter pw = new PrintWriter(out);
        
            System.out.println("[LOGGER]: dato ricevuto: " + dato);

            pw.println("Dato ricevuto: " + dato);
            pw.close();
        
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}
