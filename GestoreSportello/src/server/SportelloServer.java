package server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.IGestoreSportello;
import interfaces.ISportello;

/*
 * La classe SportelloServer ha il compito di:
 * 
 *  1.	acquisire tramite RMI Registry un oggetto remoto di tipo IGestoreSportello;
 * 	2.	istanziare uno ISportello, i cui metodi sono definiti nell'interfaccia e implementati dalla classe SportelloImpl;
 * 	3.	sottoscrivere ISportello creato come observer all'oggetto remoto IGestoreSportello acquisito. 
 */

public class SportelloServer {
	
	public static void main (String[] args) {

		try {
			// 1.1. Acquisizione del RMI Registry
			Registry rmiRegistry = LocateRegistry.getRegistry();

			// 1.2. Acquisizione dello stub
			IGestoreSportello stub = (IGestoreSportello) rmiRegistry.lookup("gestore");

			// 2. Creazione dell'istanza ottoscrizione
			ISportello sportello = new SportelloImpl();

			// 3. Sottoscrizione tramite Remote Method Invocation
			stub.sottoscrivi(sportello);
			System.out.println("[SERVER-SPORTELLO]: Sportello sottoscritto al gestore.");
		}
		catch (RemoteException e){
			e.printStackTrace();
		}
		catch (NotBoundException e){
			e.printStackTrace();
		}

		
	}

}
