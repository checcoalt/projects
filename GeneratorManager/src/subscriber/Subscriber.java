package subscriber;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import service.IManager;
import service.IObserver;

public class Subscriber {
    
    public static void main(String[] args) {

        if (args.length < 3){
            System.err.println("[SUBSCRIBER ERROR]: requires at least 3 arguments: <componentID>, <port>, <filename>");
            System.exit(1);
        }

        try {
            int componentID = Integer.parseInt(args[0]);
            int port = Integer.parseInt(args[1]);
            String filename = args[2];
            
            Registry rmiRegistry = LocateRegistry.getRegistry();
            IManager manager = (IManager) rmiRegistry.lookup("manager");
            manager.subscribe(componentID, port);
            System.out.println("[SUBSCRIBER]: running and attached ...");
            
            IObserver subscriber = new SubscriberImpl(filename);
            SubscriberSkeleton skeleton = new SubscriberSkeleton(subscriber, port);
            skeleton.runSkeleton();
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
        catch (NotBoundException e){
            e.printStackTrace();
        }

    }

}
