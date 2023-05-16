package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import service.IMagazzino;

public class ClientA {

    public static final int N_THREADS = 5;      // Numero di thread generati
    public static final int N_REQUESTS = 3;     // Numero di richieste per ogni thread

    public static void main(String[] args) {
        
        /*
         * Da terminale in C:\PATH\src\bin:
         *      > java client.ClientA IP_ADDRESS PORT
         *  es. > java client.ClientA  127.0.0.1 8000
         */

        if (args.length < 2){
            System.err.println("[CLIENT-A]: numero di argomenti insufficiente: IP_ADDRESS e PORT_NUMBER richiesti.");
            System.exit(1);
        }

        else {

            try {
            
                // Lettura dell'indirizzo ip e del port number da CLI
                InetAddress addr = InetAddress.getByName(args[0]);
                int port = Integer.parseInt(args[1]);

                // Istanza di IMagazzino (proxy, che si occupa dell'inoltro e della ricezione)
                IMagazzino magazzino = new MagazzinoProxy(addr, port);

                // Insieme di thread
                ClientAThread [] threads = new ClientAThread[N_THREADS];

                // Generazione e avvio dei thread che invocano deposita()
                for (int i = 0; i < N_THREADS; i++){
                    threads[i] = new ClientAThread(magazzino, N_REQUESTS);
                    System.out.println("[CLIENT-A]: Thread " + i + " avviato.");
                    threads[i].start();
                }

            }

            catch (UnknownHostException e){
                System.err.println("[CLIENT-A]: Host non trovato.");
                e.printStackTrace();
            }
        }

    }
}
