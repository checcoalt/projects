package manager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;

import service.AlertNotification;
import service.IManager;
import service.IObserver;

public class ManagerImpl extends UnicastRemoteObject implements IManager {

    private Hashtable <Integer, ArrayList<Integer>> subs; 

    public ManagerImpl() throws RemoteException {
        subs = new Hashtable<Integer, ArrayList<Integer>>();
    }

    public void subscribe(int componentID, int port) {
        
        if (subs.get(componentID) == null) {
            ArrayList<Integer> ports = new ArrayList<Integer>();
            ports.add(port);
            subs.put(componentID, ports);

            System.out.println("[MANAGER]: new subscriber with a new ID: " + componentID + ": now they are " + subs.size());
        }
        else {
            subs.get(componentID).add(port);
            System.out.println("[MANAGER]: new subscriber with existing ID: " + componentID + " --> (" + subs.get(componentID).size() + ")");
        }
    }

    public synchronized void sendNotification(AlertNotification an) {

        int componentID = an.getComponentID();
        int criticality = an.getCriticality();

        System.out.println("[MANAGER]: new notification received: " + "[" + componentID + ", " + criticality + "]");
        
        if (subs.get(componentID) == null) System.out.println("[MANAGER]: don't have subscriptions for this ID");
        else {
            ArrayList<Integer> ports = new ArrayList<Integer>(subs.get(componentID));

            for (int port : ports) {
                IObserver proxy = new ManagerProxy(port);
                proxy.notifyAlert(criticality);
            } 
        }      
    }
    
}
