package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.IGestoreSportello;

/*
 * La classe GestoreServer ha il compito di associare un oggetto IGestoreSportello ad un servizio remoto tramite RMI Registry.
 * Il nome usato nel rebind() dovrà essere lo stesso richiesto da Client e SportelloServer nella lookup().
 */

public class GestoreServer {
	
	public static void main (String[] args) {
		
		try {

			// 1. Istanziazione di un gestore
			IGestoreSportello gestore = new GestoreSportelloImpl();

			// 2. Registrazione del servizio remoto
			Registry rmiRegistry = LocateRegistry.getRegistry();
			rmiRegistry.rebind("gestore", gestore);

			System.out.println("[SERVER-GESTORE]: registrazione del servizio effettuata.");


		}
		catch (RemoteException e){
			e.printStackTrace();
		}
		
	}

}
