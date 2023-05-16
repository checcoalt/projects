package client;

import java.util.Random;

import service.IMagazzino;

/*
 * Invoca i metodi sul proxy.
 */

public class ClientAThread extends Thread {

    private IMagazzino magazzino;       // Istanza del magazzino (è il proxy)
    private int requests;               // Numero di richieste per thread

    public ClientAThread(IMagazzino magazzino, int requests){
        this.magazzino = magazzino;
        this.requests = requests;
    }

    public void run(){

        for(int i = 0; i < requests; i++){

            // Generazione randomica di articolo, id e tempo di attesa
            Random random = new Random();
            
            String articolo = 1 + random.nextInt(2) == 1 ? "laptop" : "smartphone";
            
            int id = 1 + random.nextInt(100);

            int waitingTime = (2 + random.nextInt(3)) * 1000;

            try {
                Thread.sleep(waitingTime);
            }
            catch (InterruptedException e){
                System.err.println("[CLIENT-A-THREAD]: errore nella Thread.sleep().");
                e.printStackTrace();
            }

            // Invocazione del metodo sull'istanza (proxy)
            if (magazzino.deposita(articolo, id))
                System.out.println("[CLIENT-A-THREAD]: depositato " + articolo + " con id " + id);

            else System.out.println("[CLIENT-A-THREAD]: errore nella chiamata deposita().");
        }

    }
    
}
