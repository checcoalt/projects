package dispatcher;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import service.IDispatcher;

public class Dispatcher {

    public static void main(String[] args) {

        try {
            // Bind the dispatcher to the RMI registry
            Registry rmiRegistry = LocateRegistry.getRegistry();
            IDispatcher dispatcher = new DispatcherImpl();
            rmiRegistry.rebind("dispatcher", dispatcher);

            System.out.println("[DISPATCHER]: running ...");
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        
    }
    

}
