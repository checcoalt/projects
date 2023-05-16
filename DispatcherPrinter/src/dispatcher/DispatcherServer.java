package dispatcher;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import service.IDispatcher;

/*
 * La classe DispatcherServer è la classe Runnable del dispatcher,
 * che ha il compito di istanziare un IDispatcher e di esporne i servizi
 * tramite registrazione sull'RMI Registry.
 * 
 */

public class DispatcherServer {
    
    public static void main(String[] args) {

        try {

            // Creazione dell'oggetto remoto
            IDispatcher dispatcher = new DispatcherImpl();

            // Acquisizione del RMI Registry
            Registry rmiRegistry = LocateRegistry.getRegistry();

            // Registrazione del servizio
            rmiRegistry.rebind("dispatcher", dispatcher);
            System.out.println("[DISPATCHER-SERVER]: Server started.");
    
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
    }
}
