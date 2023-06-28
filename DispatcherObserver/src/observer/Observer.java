package observer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import service.IDispatcher;
import service.IObserver;

public class Observer {

    public static void main(String[] args) {

        if (args.length < 2){
            System.out.println("[OBSERVER]: need 2 arguments <type> <filename>.");
            System.exit(1);
        }

        try {
            Registry rmiRegistry = LocateRegistry.getRegistry();
            IDispatcher dispatcher = (IDispatcher) rmiRegistry.lookup("dispatcher");
            IObserver obs = new ObserverImpl(dispatcher, args[1]);
            dispatcher.attach(obs, args[0]);
            
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        catch (NotBoundException e) {
            e.printStackTrace();
        }

        System.out.println("[OBSERVER]: running and attached ...");

    }
    
}
