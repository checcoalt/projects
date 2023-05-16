package node;

import java.rmi.RemoteException;
import java.util.Random;

import service.IDispatcher;

/*
 * La classe NodeThread si occupa di sottoporre le richieste al dispatcher,
 * il cui riferimento è ricevuto come parametro del costruttore e corrisponde
 * allo stub acquisito dalla classe Node a partire dal RMI Registry.
 * 
 * Le richieste sono sottoposte sotto forma di Remote Method Invocation.
 */

public class NodeThread extends Thread {

    private int requests;               // Numero di richieste per thread
    private IDispatcher dispatcher;     // Già istanziato da Node (ricevuto come stub dal RMI Registry)

    public NodeThread(int requests, IDispatcher dispatcher){
        this.requests = requests;
        this.dispatcher = dispatcher;
    }

    public void run() {

        try{

            for (int i = 0; i < requests; i++){

                // Per la generazione casuale del tempo di attesa e del nome del documento
                Random random = new Random();

                // Attesa del thread (operazione lenta)
                int waitingTime = (1 + random.nextInt(3)) * 1000;
                Thread.sleep(waitingTime);

                // Creazione della stringa con il nome del documento da sottoporre alla stampante
                String docName = "doc" + (1 + random.nextInt(50));

                System.out.println("[NODE-THREAD]: Print request: " + docName);

                // Attesa dell'esito
                boolean result = dispatcher.printRequest(docName);

                System.out.println("[NODE-THREAD]: Print request " + docName + " had result " + result);
            }
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }
    
}
