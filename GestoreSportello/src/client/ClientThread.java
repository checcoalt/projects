package client;


import java.rmi.RemoteException;
import java.util.Random;

import interfaces.IGestoreSportello;

/*
 * La classe ClientThread implementa l'attività del thread, che sottopone le richieste
 * allo stub del gestore ricevuto dal Client tramite chiamata remota.
 */

public class ClientThread extends Thread {

	private int requests;
	private IGestoreSportello gestore;
	
	public ClientThread(int requests, IGestoreSportello gestore) {
		
		this.requests = requests;
		this.gestore = gestore;
		
	}
	
	public void run() {

		Random random = new Random();
		
		try {
			for (int i = 0; i < requests; i++){

				// Attesa di [1,3] secondi
				int waitingTime = (1 + (int) Math.random() * 3) * 1000;
				Thread.sleep(waitingTime);

				// Setting dell'id client
				int idClient = 1 + random.nextInt(100);

				// Richiesta (come Remothe Method Invocation)
				boolean result = gestore.sottoponiRichiesta(idClient);
				System.out.println("[CLIENT-THREAD]: richiesta " + idClient + " sottoposta al gestore e servita con esito " + result);
				
			}
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}
		catch (RemoteException e){
			e.printStackTrace();
		}
		
	}
	
	

}
