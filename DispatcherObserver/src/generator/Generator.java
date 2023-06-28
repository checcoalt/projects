package generator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import service.IDispatcher;

public class Generator {

    private static final int NUM_THREADS = 3;
    private static final int NUM_INVOKE = 3;


    public static void main(String[] args) {

        try {
            Registry rmiRegistry = LocateRegistry.getRegistry();
            IDispatcher dispatcher = (IDispatcher) rmiRegistry.lookup("dispatcher");
        
            GeneratorThread [] t = new GeneratorThread[NUM_THREADS];

            for (int i = 0; i < NUM_THREADS; i++) {
                t[i] = new GeneratorThread(dispatcher, NUM_INVOKE);
                System.out.println("[GENERATOR]: new thread starting (" + i + ") ...");
                t[i].start();
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
