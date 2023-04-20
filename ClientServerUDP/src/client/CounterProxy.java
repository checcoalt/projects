package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import service.ICounter;

public class CounterProxy implements ICounter {
    
    private DatagramSocket socket; // per la comunicazione con lo Skeleton: devono comunicare sullo stesso porto

    public CounterProxy(){

        try{
            socket = new DatagramSocket();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void setCount(String id, int s){

        // Costruzione del messaggio come definito per convenzione
        String message = new String ("setCount#" + id + "#" + s + "#");

        try{
            // Costruzione ed invio del pacchetto
            DatagramPacket request = new DatagramPacket(message.getBytes(),         // bytes
                                                        message.getBytes().length,  // length
                                                        InetAddress.getLocalHost(), // host
                                                        9000);                 // port
            socket.send(request);

            // Attesa della risposta
            byte[] buffer = new byte [100];                                     // buffer di ricezione
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);   // pacchetto di ricezione, che scrive sul buffer
            socket.receive(reply);
            // è una receive bloccante: il processo attende su questa riga di codice finché non arriva una risposta

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public int sum(int s){

        int x = 0;
        String message = new String ("sum#" + s + "#");

        try{
            DatagramPacket request = new DatagramPacket(message.getBytes(),
                                                        message.getBytes().length,
                                                        InetAddress.getLocalHost(),
                                                        9000);
            socket.send(request);

            byte[] buffer = new byte [100];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);

            // Parsing della risposta per leggere il valore restituito dal metodo
            String replyMessage = new String(reply.getData(), 0, reply.getLength());
            x = Integer.valueOf(replyMessage).intValue();
            // [Tipo generico] --(Integer.valueof())--> [Integer] --(.intvalue())--> [int]

            return x;

        } catch(IOException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public int increment(){

        int x = 0;
        String message = new String ("increment#");

        try{
            DatagramPacket request = new DatagramPacket(message.getBytes(),
                                                        message.getBytes().length,
                                                        InetAddress.getLocalHost(),
                                                        9000);
            socket.send(request);

            byte[] buffer = new byte [100];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);

            String replyMessage = new String(reply.getData(), 0, reply.getLength());
            x = Integer.valueOf(replyMessage).intValue();

            return x;

        } catch(IOException e) {
            e.printStackTrace();
        }

        return 0;

    }

}
