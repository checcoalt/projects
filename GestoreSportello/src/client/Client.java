package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.IGestoreSportello;

/*
 * La classe Client ha il compito di acquisire uno stub del gestore dal RMI Registry
 * e generare i Thread che sottoporranno le richieste, a cui passerà lo stub come parametro del costruttore.
 */

public class Client {
	
	public static void main (String[] args) {

		final int T = 10;		// Numero di thread
		final int R = 10;		// Numero di richieste per thread

		try {
		
			// 1.1. Acquisizione del RMI Registry
			Registry rmiRegistry = LocateRegistry.getRegistry();

			// 1.2. Acquisizione dello stub gestore
			IGestoreSportello gestore = (IGestoreSportello) rmiRegistry.lookup("gestore");

			// 2.1. Vettore di T thread
			ClientThread [] threads = new ClientThread[T];

			// 2.2. Creazione ed avvio dei singoli thread
			for (int i = 0; i < T; i++) {
				threads[i] = new ClientThread(R, gestore);
				threads[i].start();
			}
		
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
		catch(NotBoundException e){
			e.printStackTrace();
		}
		
	}

}
