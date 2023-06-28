package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import interfaces.IGestoreSportello;
import interfaces.ISportello;

/*
 * La classe GestoreSportelloImpl implementa i metodi definiti dall'interfaccia IGestoreSportello.
 * Tra questi metodi c'è il metodo sottoscrivi(), che sarà invocato sul gestore come chiamata remota
 * da parte del Server degli sportelli, per sottoscrivere il suo sportello e mettersi in osservazione come Observer,
 * e il metodo sottoponiRichiesta(), che sarà invocato tramite chiamata a oggetto remoto dal Client.
 */


public class GestoreSportelloImpl extends UnicastRemoteObject implements IGestoreSportello {

	// Vettore che mantiene l'elenco degli sportelli sottoscritti come Observer
	private Vector<ISportello> sportelli;

	protected GestoreSportelloImpl() throws RemoteException {
		// Istanziazione del vettore
		sportelli = new Vector<ISportello>();
	}

	@Override
	public boolean sottoponiRichiesta(int idCliente) throws RemoteException {

		boolean result = false;

		// Foreach sportello nella lista di sportelli
		for (ISportello sportello : sportelli) {

			// Se lo sportello ha modo di servire la richiesta (cioè ha permessi disponibili sui semafori)
			if (sportello.serviRichiesta(idCliente)){
				result = true;
				break;
			}

		}

		System.out.println("[GESTORE]: Servizio richiesto dal cliente " + idCliente + " evaso con esito " + result);

		return result;
	
	}

	@Override
	public void sottoscrivi(ISportello sportello) throws RemoteException{

		// Aggiunta dello sportello alla lista degli Observer.
		// Questo metodo viene invocato sul gestore come oggetto remoto da parte del Server che mantiene lo sportello.		
		sportelli.add(sportello);
		System.out.println("[GESTORE]: Sportello aggiunto alla lista di sottoscrizioni.");
	
	}

}
