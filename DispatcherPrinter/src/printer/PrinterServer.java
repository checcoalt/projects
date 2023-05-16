package printer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import service.IDispatcher;
import service.IPrinter;

/*
 * La classe PrinterServer rappresenta la classe Runnable del servizio IPrinter.
 * 
 * Essendo tale servizio coinvolto in un duplice meccanismo di comunicazione,
 * uno basato su socket TCP e pattern Proxy-Skeleton, e l'altro basato su RMI,
 * la classe ha un duplice compito:
 * 
 *      -    avviare uno skeleton che gestisca la comunicazione su socket;
 * 
 *      -    acquisire dal RMI Registry uno stub del dispatcher,
 *           a cui si sottoscrive con il metodo remoto addPrinter(),
 *           con cui fornisce il proprio indirizzo ip e numero di porto.
 * 
 * Il programma legge il numero di porto e il nome del file su cui vanno stampati i valori da riga di comando:
 * es.
 *      java printer.PrinterServer 8000 PrinterDocuments.txt
 * 
 */

public class PrinterServer {

    public static void main(String[] args) {
        
        try {

            // Verifica della corretta chiamata da riga di comando
            if(args.length < 2){
                System.err.println("[PRINTER-SERVER]: NOT ENOUGH ARGUMENTS");
                System.exit(1);
            }

            else {

                // Parsing degli argomenti da riga di comando
                int port = Integer.parseInt(args[0]);
                String filename = args[1];

                // Creazione di un riferimento alla stampante
                IPrinter printer = new PrinterImpl(filename);

                // Collegamento della stampante al proprio skeleton
                PrinterSkeleton skeleton = new PrinterSkeleton(printer, port);

                // Acquisizione di uno stub del dispatcher
                Registry rmiRegistry = LocateRegistry.getRegistry();
                IDispatcher stub = (IDispatcher) rmiRegistry.lookup("dispatcher");

                // Sottoscrizione della stampante (con il suo indirizzo ip e porto) all'elenco tenuto dal dispatcher
                stub.addPrinter(InetAddress.getLocalHost(), port);

                System.out.println("[PRINTER-SERVER]: subscribed to Dispatcher.");
                System.out.println("[PRINTER-SERVER]: Server started.");

                // Attivazione del servizio su socket della stampante
                skeleton.runSkeleton();

            }
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
        catch (NotBoundException e){
            e.printStackTrace();
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }

    }
    
}
