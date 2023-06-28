package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import service.ILogger;

public class LoggerSkeleton {

    private ILogger logger;
    private DatagramSocket socket;

    public LoggerSkeleton(ILogger logger, int port) {
        try {
            this.logger = logger;
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    public void runSkeleton() {

        try {
            while(true) {

                byte [] bytes = new byte[Integer.BYTES];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                       
                socket.receive(packet);

                int dato = ByteBuffer.wrap(bytes).getInt();
            
                logger.registraDato(dato);

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
