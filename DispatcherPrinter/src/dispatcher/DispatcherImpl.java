package dispatcher;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import service.IDispatcher;
import service.IPrinter;
import service.TCP_IP_DATA;

/*
 * La classe DispatcherImpl fornisce un'implementazione dell'interfaccia IDispatcher,
 * che espone servizi accessibili da remoto tramite RMI Registry.
 * 
 * In particolare, implementa il pattern Observer, in cui le stampanti si registrano come disponibili,
 * attraverso il metodo addPrinter(), fornendo il proprio indirizzo ip e numero di porto.
 * 
 * Al momento della printRequest(), il dispatcher crea un'istanza di IPrinter rappresentata dal proprio Proxy,
 * a cui associa l'indirizzo ip ed il numero di porto ricevuto tramite chiamata remota:
 * ha ricostruito un riferimento basato su socket della stessa IPrinter che si era sottoscritta con addPrinter().
 * 
 */

public class DispatcherImpl extends UnicastRemoteObject implements IDispatcher {

    // Vettore delle stampanti, memorizzate tramite gli indirizzi ip e i numeri di porto ad esse associati
    private Vector<TCP_IP_DATA> printers;    

    public DispatcherImpl() throws RemoteException {
        printers = new Vector<TCP_IP_DATA>();
    }
    
    public void addPrinter(InetAddress address, int port) throws RemoteException {
        
        // Sottoscrizione fornendo il proprio indirizzo ip e numero di porto
        TCP_IP_DATA location = new TCP_IP_DATA(address, port);
        printers.add(location);
        System.out.println("[DISPATCHER]: Printer on address " +
                            location.getIpAddress() + ":" + location.getPORT() + " added to printers' list.");
        
    }
    
    public boolean printRequest(String docName) throws RemoteException {
        
        boolean result = false;
        
        // Per ogni stampante (coppia <ip, porto>) registrata sul dispatcher
        for (TCP_IP_DATA printer : printers) {

            // Crea un proxy per invocare una procedura remota sulla stampante
            IPrinter proxy = new PrinterProxy(printer.getIpAddress(), printer.getPORT());

            // Se la procedura remota restituisce valore vero sulla stampante
            // e tale valore viene correttamente trasmesso via socket (sequenza incapsulata dalla classe DispatcherProxy)
            if(proxy.print(docName)){
                System.out.println("[DISPATCHER]: found a free printer for " + docName +
                                    " (" + printer.getIpAddress() + ":" + printer.getPORT() + ").");
                result = true;
                break;
            }

            // Se arriva a questo punto, è necessario cercare un'altra stampante (se disponibile)
            System.out.println("[DISPATCHER]: can't handle request " + docName +
                                " (" + printer.getIpAddress() + ":" + printer.getPORT() + ").");
        }

        // Stampa dell'esito
        System.out.println("[DISPATCHER]: print request for " + docName + " had result " + result);

        return result;

    }

}
