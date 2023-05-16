package node;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import service.IDispatcher;

/*
 * La classe Node ha il compito di acquisire dal RMI Registry uno stub del dispatcher
 * per la chiamata remota dei metodi, e di avviare N_THREADS,
 * che sottopongono richieste al dispatcher proprio attraverso RMI.
 */

public class Node {
    public static void main(String[] args) {

        // Costanti di utilità
        final int N_THREADS = 5;
        final int N_REQUESTS = 3;

        try {
            // Acquisisce il registro e lo stub del dispatcher
            Registry rmiRegistry = LocateRegistry.getRegistry();
            IDispatcher stub = (IDispatcher) rmiRegistry.lookup("dispatcher");

            // Crea un array di thread
            NodeThread [] threads = new NodeThread[N_THREADS];

            System.out.println("[NODE]: Client started.");
            
            // Crea i singoli thread e li avvia
            for (int i = 0; i < N_THREADS; i++){
                threads[i] = new NodeThread(N_REQUESTS, stub);
                threads[i].start();
            }
        }

        catch (RemoteException e){
            e.printStackTrace();
        }
        catch (NotBoundException e){
            e.printStackTrace();
        }

    }
}
