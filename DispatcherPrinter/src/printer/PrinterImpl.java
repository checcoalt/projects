package printer;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.concurrent.Semaphore;

import service.IPrinter;

/*
 * La classe PrinterImpl concretizza lato server i metodi definiti dall'interfaccia IPrinter.
 * Ha anche il compito di gestire la concorrenza per il possesso dell'unica risorsa (stampante).
 * Il metodo print() qui definito è la reale implementazione del metodo di stampa,
 * e sarà acceduto dal proprio Skeleton (in realtà da un suo thread),
 * dopo che ne avrà accuratamente effettuato il parsing.
 * 
 */

public class PrinterImpl implements IPrinter {

    private Semaphore mutexPrinter;     // per la gestione della concorrenza sulla stampante
    private String fileName;            // nome del file su cui effettuare le operazioni

    public PrinterImpl(String fileName) throws RemoteException {

        // Inizializzazione del mutex
        mutexPrinter = new Semaphore(1);
        this.fileName = fileName;

    }
    
    public boolean print(String docName) {

        boolean result = false;

        // Risorsa occupata
        if(!mutexPrinter.tryAcquire()){
            System.out.println("[PRINTER]: Request " + docName + " refused. Busy in other activity.");
            return result;
        }

        // Risorsa libera e permesso acquisito
        else {
            try {

                System.out.println("[PRINTER]: permission acquired from require " + docName);

                // Attesa randomica [5,10] secondi
                Random random = new Random();
                int waitingTime = (5 + random.nextInt(6)) * 1000;
                Thread.sleep(waitingTime);

                // Flussi di dati per la scrittura su file (il cui nome è indicato dal costruttore)
                FileOutputStream fStream = new FileOutputStream(fileName, true);
                BufferedOutputStream buf = new BufferedOutputStream(fStream);
                PrintWriter pw = new PrintWriter(buf);

                // Scrittura su documento
                pw.println(docName);
                pw.flush();

                // Stampa a video
                System.out.println("[PRINTER]: " + docName);

                // chiusura dei flussi
                pw.close();
                buf.close();
                fStream.close();

                result = true;
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            finally {
                mutexPrinter.release();
            }

        }

        return result;

    }

}
