package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import service.IMagazzino;

/*
 * Implementazione di uno Skeleton per delega.
 * Delega: ha come attributo un'istanza di IMagazzino, e la sua implementazione dei metodi
 * dell'interfaccia consiste nell'invocare gli stessi metodi sull'istanza di IMagazzino che possiede,
 * cioè ne delega l'effettiva implementazione.
 */

public class MagazzinoSkeleton implements IMagazzino {

    private ServerSocket server;
    private final int PORT;
    private IMagazzino magazzino;

    public MagazzinoSkeleton(int port, IMagazzino magazzino) {
        this.PORT = port;               // Port number assegnato da CLI
        this.magazzino = magazzino;     // Riferimento all'istanza di IMagazzino a cui delega le implementazioni

        try {
            // Apertura della socket lato server
            server = new ServerSocket(PORT);
        }
        catch (IOException e) {
            System.err.println("[MAGAZZINO-SKELETON]: Errore nell'apertura della socket lato server.");
            e.printStackTrace();
        }
    }

    public void runSkeleton() {

        try {

            while (true) {

                // Apertura della comunicazione
                Socket client = server.accept();

                /*
                 * DEBUGGING
                 * System.out.println("[MAGAZZINO-SKELETON]: connessione accettata: " + client.getInetAddress());       
                */

                // Avvio di un thread per l'elaborazione della richiesta
                MagazzinoThread worker = new MagazzinoThread(client, magazzino);
                worker.start();

            }

        }
        catch (IOException e) {
            System.err.println("[MAGAZZINO-SKELETON]: Errore in apertura della comunicazione.");
            e.printStackTrace();
        }

    }

    // Delega delle implementazioni
    
    public boolean deposita (String articolo, int id){
        boolean result = magazzino.deposita(articolo, id);
        return result;
    }

    public int preleva (String articolo) {
        int id = magazzino.preleva(articolo);
        return id;
    }
    
}
