package client;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Client {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Usage: java client.Client <dato> <port>");
            System.exit(1);
        }

        int dato = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);

        try {
            // Inizializzazione della comunicazione JMS
            Hashtable <String, String> p = new Hashtable<String, String>();
    
            p.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            p.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
    
            p.put("queue.request", "request");
    
            // Contesto
            Context jndi = new InitialContext(p);

            // Connection factory
            QueueConnectionFactory qcf = (QueueConnectionFactory) jndi.lookup("QueueConnectionFactory");

            // Connection
            QueueConnection qc = qcf.createQueueConnection();
            qc.start();

            // Queue
            Queue request = (Queue) jndi.lookup("request");

            // Session
            QueueSession qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            // Sender
            QueueSender sender = qs.createSender(request);

            // Messaggio
            MapMessage message = qs.createMapMessage();
            message.setInt("dato", dato);
            message.setInt("port", port);

            // Invio
            sender.send(message);
            System.out.println("[CLIENT]: message sent successfully: <" + dato + ", " + port + ">");

            qc.close();
        } 
        
        catch (NamingException e) {
            e.printStackTrace();
        }

        catch (JMSException e) {
            e.printStackTrace();
        }

    }
    
}
