package server;


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import interfaces.ISportello;

/*
 * La classe SportelloImpl ha il compito di implementare (concretizzare) i metodi dell'interfaccia ISportello,
 * gestendo i vincoli di concorrenza e sincronizzazione imposti dal problema.
 */


public class SportelloImpl extends UnicastRemoteObject implements ISportello {

	// Semafori per la gestione della concorrenza
	private Semaphore maxRequires;
	private Semaphore maxServing;
	
	protected SportelloImpl() throws RemoteException {

		maxRequires = new Semaphore(5);		// Max 5 richieste (3 attive + 2 in attesa)
		maxServing = new Semaphore(3);		// Max 3 richieste attive

	}

	@Override
	public boolean serviRichiesta(int idCliente) throws RemoteException {

		try {

			// Verifica della disponibilità di sportelli, almeno in coda
			if(!maxRequires.tryAcquire()){
				// Ritorna false se non ci sono sportelli
				System.out.println("[SPORTELLO]: Limite massimo di richieste raggiunto.");
				System.out.println("[SPORTELLO]: Impossibile servire la richiesta del cliente " + idCliente);
				return false;
			}

			// Acquisizione di un permesso per evadere la richiesta
			maxServing.acquire();

			// Attività lenta: attesa per [1,5] secondi
			int waitingTime = (1 + (int) Math.random() * 5) * 1000;
			Thread.sleep(waitingTime);

			// Creazione degli oggetti per la scrittura su file
			FileOutputStream fStream = new FileOutputStream("requests.txt", true);
			BufferedOutputStream buf = new BufferedOutputStream(fStream);
			PrintWriter pw = new PrintWriter(buf);

			// Scrittura dell'identificativo del cliente sul file (è l'attività dello sportello)
			pw.println(idCliente);
			pw.flush();

			// Rimozione degli oggetti per la scrittura su file
			pw.close();
			buf.close();
			fStream.close();

			System.out.println("[SPORTELLO]: Richiesta del cliente " + idCliente + " servita correttamente.");

		}
		
		catch (InterruptedException e){
			e.printStackTrace();
			return false;
		}

		catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		// Rilascio dei permessi semaforici
		finally {
			maxServing.release();
			maxRequires.release();
		}
				
		return true;
	}

}
