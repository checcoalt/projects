package generator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import service.IManager;

public class Generator {

    private static final int N_THREADS = 3;
    private static final int N_REQUESTS = 10;

    public static void main(String[] args) {

        try {

            Registry rmiRegistry = LocateRegistry.getRegistry();
            IManager manager = (IManager) rmiRegistry.lookup("manager");

            System.out.println("[GENERATOR]: running ...");

            GeneratorThread[] threads = new GeneratorThread[N_THREADS];

            for (int i = 0; i < N_THREADS; i++) {
                threads[i] = new GeneratorThread(manager, N_REQUESTS);
                threads[i].start();
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
    
}
