package dispatcher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import service.IPrinter;

/*
 * La classe PrinterProxy implementa l'interfaccia IPrinter, rappresentando cioè una stampante
 * vista dal dispatcher, permettendo al dispatcher stesso di invocare metodi sulla stampante come
 * se fosse un oggetto remoto, attraverso la comunicazione su socket.
 * 
 * Realizza il pattern Proxy-Skeleton dal lato client, e si fa carico quindi di tutti gli
 * oneri della comunicazione su rete.
 * 
 * In particolare, PrinterProxy apre una socket sull'indirizzo e sul porto indicatogli dal dispatcher,
 * che li ha memorizzati al momento della sottoscrizione della stampante, ed effettua il marshalling
 * dei metodi e dei parametri tramite comunicazione su socket TCP.
 * 
 */

public class PrinterProxy implements IPrinter {

    private Socket socket;

    public PrinterProxy(InetAddress addr, int port){

        try {
            // Crea la socket client
            socket = new Socket(addr, port);            
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean print(String docName){

        boolean result = false;

        try {

            // Acquisizione dei flussi di I/O dalla socket TCP
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            // Scrittura del metodo e del parametro
            out.writeUTF("print");
            out.writeUTF(docName);
            out.flush();

            // Lettura del risultato restituito dalla chiamata della funzione sulla stampante
            result = in.readBoolean();

            // Chiusura della socket
            socket.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }
    
}
