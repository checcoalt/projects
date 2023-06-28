package client;

import dispatcher.*;

public class Client {

	private final static int N_THREADS = 5;
    private final static int N_REQUESTS = 3;

	public static void main(String[] args) {		
		/*
		 * uso: 		java client.Client IP porta
		 * per es.:		java client.Client 127.0.0.1 8000
		 */

		
		/*
		 * TODO: realizzare l'implementazione con N thread client
		 */
		
		
		IDispatcher dispatcher = new DispatcherProxy ( args[0], Integer.valueOf( args[1]) );

		ClientThread threads[] = new ClientThread[5];

		for (int i = 0; i < N_THREADS; i++) {
			threads[i] = new ClientThread(dispatcher, N_REQUESTS);
			threads[i].start();
		}

		for (int i = 0; i < N_THREADS; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("[CLIENT]: Esecuzione terminata!");

	}

}
