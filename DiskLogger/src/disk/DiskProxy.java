package disk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import service.ILogger;

public class DiskProxy implements ILogger {

    private DatagramSocket socket;
    private int port;

    public DiskProxy(int port) {

        try {
            // Inizializzazione della connessione
            this.port = port;
            this.socket = new DatagramSocket();
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void registraDato(int dato) {

        try {
            // Conversione ad array di bytes
            byte [] bytes = new byte[Integer.BYTES];
            ByteBuffer.wrap(bytes).putInt(dato);
        
            // Costruzione del pacchetto
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), port);

            // Invio del pacchetto
            socket.send(packet);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}
