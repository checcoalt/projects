package observer;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import service.IDispatcher;
import service.Reading;

import service.IObserver;

public class ObserverImpl extends UnicastRemoteObject implements IObserver {

    private IDispatcher dispatcher;
    private String filename;

    public ObserverImpl(IDispatcher disp, String filename) throws RemoteException {     
        this.dispatcher = disp;
        this.filename = filename;
    }

    public void getReading() throws RemoteException {

        try {
            Reading r = this.dispatcher.getReading();
            System.out.println("[OBSERVER]: read " + r.getTipo() + " - " + r.getValore());

            FileOutputStream file = new FileOutputStream(filename, true);
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(file));

            pw.println(r.toString());

            pw.close();
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void notifyReading() throws RemoteException {
        System.out.println("[OBSERVER]: have been notified by dispatcher.");
        this.getReading();
    }
    
}
