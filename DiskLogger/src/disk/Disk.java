package disk;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Disk {


    public static void main(String[] args) {
        
        try {
            // inizializza la comunicazione JMS
            Hashtable<String, String> p = new Hashtable<String, String>();

            p.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            p.put("java.naming.provider.url", "tcp://127.0.0.1:61616");

            p.put("queue.request", "request");

            // Creazione del contesto
            Context jndi = new InitialContext(p);

            // Connection Factory
            QueueConnectionFactory qcf = (QueueConnectionFactory) jndi.lookup("QueueConnectionFactory");

            // Connection
            QueueConnection qc = qcf.createQueueConnection();

            // Queue
            Queue request = (Queue) jndi.lookup("request");

            // Session
            QueueSession qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            // Receiver
            QueueReceiver receiver = qs.createReceiver(request);

            // Crea il listener e si mette in ascolto
            DiskListener listener = new DiskListener();
            receiver.setMessageListener(listener);

            System.out.println("[DISK]: listening ...");

            // Avvio della connessione
            qc.start();


        }
        catch (JMSException e){
            e.printStackTrace();
        }
        catch (NamingException e){
            e.printStackTrace();
        }

    }
    
}
