package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import service.IMagazzino;

/*
 * Fa il marshalling delle richieste al server IMagazzino e l'unmarshalling delle risposte.
 * Richiede al client l'indirizzo IP e il porto del server a cui connettersi.
 */

public class MagazzinoProxy implements IMagazzino {

    // Locazione in rete
    private InetAddress ipAddress;
    private int port;

    public MagazzinoProxy(InetAddress addr, int port){
        this.ipAddress = addr;
        this.port = port;
    }

    public boolean deposita(String articolo, int id) {

        boolean result = false;

        try {

            // Apertura della comunicazione su socket
            Socket socket = new Socket(ipAddress, port);

            // Aggancia i flussi di I/O della socket
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Invio del metodo
            out.writeUTF("deposita");
            out.flush();

            // Invio del primo parametro
            out.writeUTF(articolo);
            out.flush();

            // Invio del secondo parametro
            out.writeInt(id);
            out.flush();

            // Attesa dell'esito in risposta
            result = in.readBoolean();

            // Chiusura dei flussi e della socket
            in.close();
            out.close();
            socket.close();

        }

        catch (IOException e){
            System.err.println("[MAGAZZINO-PROXY]: errore in fase di marshalling della richiesta deposita().");
            e.printStackTrace();
        }

        return result;

    }

    public int preleva(String articolo) {

        int id_response = -1;

        try {

            // Apertura della comunicazione su socket
            Socket socket = new Socket(ipAddress, port);

            // Aggancia i flussi di I/O della socket
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Invio del metodo
            out.writeUTF("preleva");
            out.flush();

            // Invio del primo parametro
            out.writeUTF(articolo);
            out.flush();

            // Attesa della risposta
            id_response = in.readInt();

            // Chiusura dei flussi e della socket
            in.close();
            out.close();
            socket.close();

        }

        catch (IOException e){
            System.err.println("[MAGAZZINO-PROXY]: errore in fase di marshalling della richiesta preleva().");
            e.printStackTrace();
        }

        return id_response;

    }
    
}
