package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import service.IMagazzino;

/*
 * Thread lanciato all'apertura di ogni socket da parte dello Skeleton.
 * Effettua unmarshalling delle richieste e marshalling dei valori di ritorno.
 */

public class MagazzinoThread extends Thread {

    private Socket client;
    private IMagazzino magazzino;

    public MagazzinoThread(Socket socket, IMagazzino magazzino){
        this.client = socket;           // Socket aperta dallo Skeleton
        this.magazzino = magazzino;     // Rifeirmento all'implementazione di IMagazzino in locale
    }

    public void run(){

        try {

            // Aggancia i flussi di I/O della socket
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            // Lettura del metodo invocato da remoto
            String method = in.readUTF();

            // parsing ed invocazione del metodo deposita()
            if (method.equals("deposita")){

                // Preleva i parametri
                String articolo = in.readUTF();
                int id = in.readInt();

                // Invoca il metodo boolean deposita()
                boolean result = magazzino.deposita(articolo, id);

                // Invio dell'esito
                out.writeBoolean(result);
                out.flush();


            }

            // parsing ed invocazione del metodo preleva() con invio della risposta
            else if (method.equals("preleva")){

                // Preleva il parametro
                String articolo = in.readUTF();

                // Invoca il metodo int preleva()
                int id_response = magazzino.preleva(articolo);

                // Invio della risposta
                out.writeInt(id_response);
                out.flush();

            }

            // errore nel parsing: metodo non riconosciuto
            else {
                System.err.println("[MAGAZZINO-THREAD]: Metodo non riconosciuto.");
            }

            // chiusura dei flussi e della socket
            in.close();
            out.close();
            client.close();

        }
        catch (IOException e) {
            System.err.println("[MAGAZZINO-THREAD]: Errore durante la fase di marshalling / unmarshalling.");
            e.printStackTrace();
        }                

    }
    
}
