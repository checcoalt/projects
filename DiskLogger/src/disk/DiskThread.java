package disk;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import service.ILogger;

public class DiskThread extends Thread {

    private Message m;

    public DiskThread(Message m) {
        this.m = m;
    }

    public void run() {
        try {
            // Type casting a MapMessage
            MapMessage message = (MapMessage) m;

            // Lettura dei campi (dato, porto)
            int dato = message.getInt("dato");
            int port = message.getInt("port");

            // Stampa a video
            System.out.println("Messaggio ricevuto:\n\tdato: " + dato + "\n\tport: " + port);

            // Creazione del proxy
            ILogger proxy = new DiskProxy(port);

            // Invio del messaggio invocando metodo su proxy
            proxy.registraDato(dato);
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
}
