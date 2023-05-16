package client;

import java.util.Random;

import service.IMagazzino;

/*
 * Invoca i metodi sul proxy.
 */

public class ClientBThread extends Thread {

    private IMagazzino magazzino;       // Istanza del magazzino (è il proxy)
    private int requests;               // Numero di richieste per thread

    public ClientBThread(IMagazzino magazzino, int requests){
        this.magazzino = magazzino;
        this.requests = requests;
    }

    public void run(){

        for(int i = 0; i < requests; i++){

            // Generazione randomica di articolo e tempo di attesa
            Random random = new Random();

            String articolo = 1 + random.nextInt(2) == 1 ? "laptop" : "smartphone";

            int waitingTime = (2 + random.nextInt(3)) * 1000;

            try {
                Thread.sleep(waitingTime);
            }
            catch (InterruptedException e){
                System.err.println("[CLIENT-B-THREAD]: errore nella Thread.sleep().");
                e.printStackTrace();
            }

            // Invocazione del metodo sull'istanza (proxy)
            int id_response = magazzino.preleva(articolo);

            System.out.println("[CLIENT-B-THREAD]: prelevato " + articolo + " con id " + id_response);
        }

    }
    
}