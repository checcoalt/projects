package printer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import service.IPrinter;

/*
 * La classe PrinterThread rappresenta la sequenza di operazioni svolte
 * da un thread lato server ogni volta che viene accolta.
 * 
 * Riceve dallo Skeleton la socket agganciata al sopraggiungere della richiesta e la IPrinter a cui lo Skeleton fa capo,
 * ed esegue l'unmarshalling del metodo e dei parametri, invocando il metodo di stampa vero e proprio
 * sulla IPrinter, attendendo l'esito dell'operazione e comunicandolo come valore di ritorno sulla socket TCP.
 */

public class PrinterThread extends Thread {

    private Socket client;      // già aperta da PrinterSkeleton
    private IPrinter printer;   // già istanziata da PrinterSkeleton

    public PrinterThread(Socket s, IPrinter printer){
        // Associazione delle strutture dati ricevute al momento dell'istanziazione
        this.client = s;
        this.printer = printer;
    }

    public void run(){

        try {

            // Flussi di dati per la comunicazione su socket TCP
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            // Lettura della prima stringa (necessariamente il metodo "print")
            String method = in.readUTF();

            // Stampa e restituisci vero alla chiamata remota
            if (method.equals("print")){
                
                // Lettura della seconda stringa (il parametro)
                String docName = in.readUTF();

                System.out.println("[PRINTER-THREAD]: I'm working on request " + docName);

                // Ricezione dell'esito da parte della chiamata del metodo sulla IPrinter vera e propria
                boolean result = printer.print(docName);

                // Comunicazione dell'esito su socket
                out.writeBoolean(result);
            }

            // Se il metodo non è riconosciuto (mai in questo programma)
            else {
                System.err.println("[PRINTER-SKELETON]: Unrecognized method.");
                out.writeBoolean(false);
                System.exit(1);
            }

            // Chiusura della socket (che è la stessa dello Skeleton)
            client.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
}
