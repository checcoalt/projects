package printer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import service.IPrinter;

/*
 * La classe PrinterSkeleton ha il compito di gestire la comunicazione su rete
 * per conto della classe vera e propria che implementa l'interfaccia IPrinter.
 * 
 * Lo Skeleton si mette in ascolto sulla socket e sul porto stabiliti, e avvia
 * un thread al sopraggiungere di ogni richiesta, che a sua volta implementa
 * le operazioni da svolgere per l'evasione delle richieste sulla socket appena agganciata.
 * 
 */

public class PrinterSkeleton {
    
    private ServerSocket server;    // Socket lato server per mettersi in ascolto
    private final int PORT;         // Numero di porto su cui il server è in ascolto
    private IPrinter printer;       // Istanza IPrinter che implementa per delega i metodi dell'interfaccia

    public PrinterSkeleton(IPrinter printer, int PORT){  

        this.printer = printer;
        this.PORT = PORT;
        
        try{
            server = new ServerSocket(this.PORT);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public void runSkeleton() {

        try {

            while (true) {

                // Apertura del flusso di comunicazione con il dispatcher
                Socket client = server.accept();

                // Avvio di un thread per la gestione della richiesta
                // Si potrebbe usare un ThreadPool per limitare il numero massimo di richieste
                PrinterThread serverThread = new PrinterThread(client, printer);
                serverThread.start();

            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

}
