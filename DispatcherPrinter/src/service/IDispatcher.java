package service;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IDispatcher extends Remote, Serializable {

    public void addPrinter(InetAddress address, int port) throws RemoteException;
    public boolean printRequest(String docName) throws RemoteException;

}
