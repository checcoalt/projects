package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;

import service.ICounter;

public class CounterWorker extends Thread {

    private DatagramSocket socket;
    private DatagramPacket request;
    private ICounter skeleton;

    public CounterWorker (DatagramSocket s, DatagramPacket r, ICounter sk){
        this.socket = s;
        this.request = r;
        this.skeleton = sk;
    }

    public void run() {

        String message = new String(request.getData(), 0, request.getLength());

        System.out.println("\n[CounterWorker] Processing packet:\n" + 
                            "-> request size = " + request.getLength() + "\n" + 
                            "-> message = " + message);

        StringTokenizer messageTokens = new StringTokenizer(message, "#");
        String method = messageTokens.nextToken();

        if(method.compareTo("setCount") == 0) {
            // A seconda del metodo, gli N token successivi sono i parametri dell'invocazione
            String id = messageTokens.nextToken();
            int x = Integer.valueOf(messageTokens.nextToken()).intValue();

            // up-call
            skeleton.setCount(id, x);

            // Creazione del messaggio/pacchetto di risposta
            String replyMessage = "ack";
            DatagramPacket reply = new DatagramPacket(replyMessage.getBytes(),
                                                      replyMessage.getBytes().length,
                                                      request.getAddress(),
                                                      request.getPort());
            
            // Invio del messaggio/pacchetto di risposta
            try {
                socket.send(reply);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (method.compareTo("sum") == 0) {
            // A seconda del metodo, gli N token successivi sono i parametri dell'invocazione
            int x = Integer.valueOf(messageTokens.nextToken()).intValue();

            // up-call (restituisce un intero)
            int res = skeleton.sum(x);

            // Creazione del messaggio/pacchetto di risposta
            String replyMessage = Integer.toString(res);
            DatagramPacket reply = new DatagramPacket(replyMessage.getBytes(),
                                                      replyMessage.getBytes().length,
                                                      request.getAddress(),
                                                      request.getPort());
            
            // Invio del messaggio/pacchetto di risposta
            try {
                socket.send(reply);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (method.compareTo("increment") == 0) {
            // Non ci sono altri token
            
            // up-call (restituisce un intero)
            int res = skeleton.increment();

            // Creazione del messaggio/pacchetto di risposta
            String replyMessage = Integer.toString(res);
            DatagramPacket reply = new DatagramPacket(replyMessage.getBytes(),
                                                      replyMessage.getBytes().length,
                                                      request.getAddress(),
                                                      request.getPort());
            
            // Invio del messaggio/pacchetto di risposta
            try {
                socket.send(reply);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else System.out.println("* BAD METHOD! *");

    } // end run
    
} // end class CounterWorker