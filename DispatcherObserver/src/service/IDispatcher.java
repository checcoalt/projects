package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IDispatcher extends Remote {

    // Interazione con il buffer remoto
    public void setReading(Reading r) throws RemoteException;
    public Reading getReading() throws RemoteException;
    
    // Pattern Observer (callback method)
    public void attach(IObserver obs, String tipo) throws RemoteException;

}
