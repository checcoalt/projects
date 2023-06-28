package server;

public class DispatcherServer {
	public static void main(String[] args) {
		
		/*
		 * avvio programma server: 	java server.DispatcherServer num_porta
		 * per es., 				java server.DispatcherServer 8000
		 */
		
		
		/*
		 * implementazione per delega
		 * 
		 * DispatcherImpl dispatcher = new DispatcherImpl ();
		 * DispatcherSkeletonD skeleton = new DispatcherSkeletonD ( dispatcher, Integer.valueOf(args[0]) );
		 * skeleton.runSkeleton();
		
		*/
		
		/*
		 * implementazione per ereditarieta'
		 */
		
		DispatcherImpl dispatcher = new DispatcherImpl ( Integer.valueOf(args[0]) );
		dispatcher.runSkeleton();
		
	}

}
