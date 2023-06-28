package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IManager extends Remote {
    public void sendNotification(AlertNotification an) throws RemoteException;
    public void subscribe (int componentID, int port) throws RemoteException;
}
