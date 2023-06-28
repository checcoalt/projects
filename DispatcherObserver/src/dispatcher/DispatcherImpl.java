package dispatcher;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.Semaphore;

import service.IDispatcher;
import service.IObserver;
import service.Reading;

public class DispatcherImpl extends UnicastRemoteObject implements IDispatcher {

    private Hashtable<IObserver, String> observers;
    
    private Semaphore sem;

    private Reading buffer;

    public DispatcherImpl() throws RemoteException {

        sem = new Semaphore(1);
        observers = new Hashtable<IObserver, String>();

    }

    public void attach(IObserver obs, String tipo) throws RemoteException {
        observers.put(obs, tipo);
        System.out.println("[DISPATCHER]: observer of kind '" + tipo +"' has been attached.");
        System.out.println("\t\tObservers: " + observers.size());
    }

    public void notifyAllReading(String tipo) throws RemoteException {

        Set <IObserver> keySet = observers.keySet();

        for (IObserver obs : keySet){
            if(observers.get(obs).equals(tipo)) {
                System.out.println("[DISPATCHER]: notifying ...");
                obs.notifyReading();
                System.out.println("[DISPATCHER]: observer of kind '" + tipo + "' has been notified.");
            }
        }
    }

    public void setReading(Reading r) throws RemoteException {

        try {
            sem.acquire();

            buffer = new Reading(r.getTipo(), r.getValore());
            System.out.println("[DISPATCHER]: element of kind '" + buffer.getTipo() + "' and value '" + r.getValore() + "' has been set.");

            notifyAllReading(buffer.getTipo());

            sem.release();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    public Reading getReading() throws RemoteException {
        return buffer;
    }
    
}
