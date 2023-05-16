package server;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import service.IMagazzino;

/*
 * Implementa il corpo effettivo dei metodi definiti dall'interfaccia.
 * Gestisce quindi anche i problemi di competizione e sincronizzazione.
 */

public class MagazzinoImpl implements IMagazzino {

    // Code circolari
    private int [] queueLaptops;
    private int [] queueSmartphones;

    // Dimensione delle code
    private final int SIZE = 5;
    
    // Lock per la sincronizzazione delle due code
    private Lock lockLaptops;
    private Lock lockSmartphones;

    // Variabili condition per la coda dei laptop
    private Condition laptopsFull;
    private Condition laptopsEmpty;

    // Variabili condition per la coda degli smartphone
    private Condition smartphonesFull;
    private Condition smartphonesEmpty;

    // Numero di elementi nelle rispettive code
    private int laptops;
    private int smartphones;

    // Elemento di testa delle rispettive code
    private int headLaptops;
    private int tailLaptops;

    // Elemento finale delle rispettive code
    private int headSmartphones;
    private int tailSmartphones;

    public MagazzinoImpl(){

        // Inizializzazione delle code
        queueLaptops = new int[SIZE];
        queueSmartphones = new int[SIZE];

        // Inizializzazione delle variabili di utilità
        laptops = smartphones = 0;
        headLaptops = headSmartphones = 0;
        tailLaptops = tailSmartphones = 0;

        // Inizializzazione dei Lock
        lockLaptops = new ReentrantLock();
        lockSmartphones = new ReentrantLock();

        // Associazione delle Condition ai rispettivi Lock
        laptopsFull = lockLaptops.newCondition();
        laptopsEmpty = lockLaptops.newCondition();
        smartphonesFull = lockSmartphones.newCondition();
        smartphonesEmpty = lockSmartphones.newCondition();

    }

    public boolean deposita(String articolo, int id){

        boolean result = false;

        if(articolo.equals("laptop")) {             // deposito in coda laptop

            // Acquisisce il lock
            lockLaptops.lock();

            try {

                // Sospende il thread fintanto che la coda è piena
                while(laptops == SIZE){
                    laptopsFull.await();
                }

                // Operazioni da eseguire in accesso esclusivo alla coda circolare
                queueLaptops[headLaptops] = id;             // Deposita l'id
                headLaptops = (headLaptops + 1) % SIZE;     // Aggiorna la testa del buffer
                laptops++;                                  // Incrementa contatore di elementi
                result = true;                              // Restituisce esito positivo

                System.out.println("[MAGAZZINO]: depositato laptop con id " + id);

                // Avverte un thread che attendeva un laptop
                laptopsEmpty.signal();
            }

            catch(InterruptedException e) {
                System.err.println("[MAGAZZINO]: errore di sincronizzazione nel deposito in coda laptop.");
                e.printStackTrace();
            }

            finally {
                // Rilascia il lock
                lockLaptops.unlock();
            }

        }

        else if(articolo.equals("smartphone")){     // deposito in coda smartphone

            // Acquisisce il lock
            lockSmartphones.lock();

            try {

                // Sospende il thread fintanto che la coda è piena
                while(smartphones == SIZE){
                    smartphonesFull.await();
                }

                // Operazioni da eseguire in accesso esclusivo alla coda circolare
                queueSmartphones[headSmartphones] = id;             // Deposita l'id
                headSmartphones = (headSmartphones + 1) % SIZE;     // Aggiorna la testa del buffer
                smartphones++;                                      // Incrementa contatore di elementi
                result = true;                                      // Restituisce esito positivo

                System.out.println("[MAGAZZINO]: depositato smartphone con id " + id);

                // Avverte un thread che attendeva uno smartphone
                smartphonesEmpty.signal();
            }

            catch(InterruptedException e) {
                System.err.println("[MAGAZZINO]: errore di sincronizzazione nel deposito in coda smartphone.");
                e.printStackTrace();
            }

            finally {
                // Rilascia il lock
                lockSmartphones.unlock();
            }

        }

        else {      // oggetto non riconosciuto
            System.err.println("[MAGAZZINO]: Errore - prodotto non riconosciuto.");
        }

        return result;
    }

    public int preleva(String articolo){

        int id_result = -1;

        try {

            // Apertura dei flussi verso file
            FileOutputStream fileStreamLaptops = new FileOutputStream("file1.txt", true);
            FileOutputStream fileStreamSmartphones = new FileOutputStream("file2.txt", true);

            // Apertura di buffer verso file per scrittura bufferizzata
            BufferedOutputStream bufLaptops = new BufferedOutputStream(fileStreamLaptops);
            BufferedOutputStream bufSmartphones = new BufferedOutputStream(fileStreamSmartphones);

            // Apertura di oggetti per scrittura su file
            PrintWriter pwLaptops = new PrintWriter(bufLaptops);
            PrintWriter pwSmartphones = new PrintWriter(bufSmartphones);


            if(articolo.equals("laptop")) {             // prelievo da coda laptop

                // Acquisisce il lock
                lockLaptops.lock();

                try {

                    // Sospende il thread fintanto che la coda è vuota
                    while(laptops == 0){
                        laptopsEmpty.await();
                    }

                    // Operazioni da eseguire in accesso esclusivo alla coda circolare
                    id_result = queueLaptops[tailLaptops];      // Preleva l'id
                    tailLaptops = (tailLaptops + 1) % SIZE;     // Aggiorna la coda del buffer
                    laptops--;                                  // Decrementa contatore di elementi

                    // Stampa a video
                    System.out.println("[MAGAZZINO]: prelevato laptop con id " + id_result);

                    // Stampa su file
                    pwLaptops.println("Prelevato laptop con id: " + id_result);
                    pwLaptops.flush();

                    // Avverte un thread che intendeva depositare un laptop
                    laptopsFull.signal();
                }

                catch(InterruptedException e) {
                    System.err.println("[MAGAZZINO]: errore di sincronizzazione nel prelievo da coda laptop.");
                    e.printStackTrace();
                }

                finally {
                    // Rilascia il lock
                    lockLaptops.unlock();
                }

            }

            else if(articolo.equals("smartphone")){     // prelievo da coda smartphone

                // Acquisisce il lock
                lockSmartphones.lock();

                try {

                    // Sospende il thread fintanto che la coda è vuota
                    while(smartphones == 0){
                        smartphonesEmpty.await();
                    }

                    // Operazioni da eseguire in accesso esclusivo alla coda circolare
                    id_result = queueSmartphones[tailSmartphones];      // Preleva l'id
                    tailSmartphones = (tailSmartphones + 1) % SIZE;     // Aggiorna la coda del buffer
                    smartphones--;                                      // Decrementa contatore di elementi

                    // Stampa a video
                    System.out.println("[MAGAZZINO]: prelevato smartphone con id " + id_result);

                    // Stampa su file
                    pwSmartphones.println("Prelevato smartphone con id: " + id_result);
                    pwSmartphones.flush();

                    // Avverte un thread che intendeva depositare uno smartphone
                    smartphonesFull.signal();
                }

                catch(InterruptedException e) {
                    System.err.println("[MAGAZZINO]: errore di sincronizzazione nel prelievo da coda smartphone.");
                    e.printStackTrace();
                }

                finally {
                    // Rilascia il lock
                    lockSmartphones.unlock();
                }

            }

            else {      // oggetto non riconosciuto
                System.err.println("[MAGAZZINO]: Errore - prodotto non riconosciuto.");
            }

            // Chiusura dei PrintWriter
            pwLaptops.close();
            pwSmartphones.close();

            // Chiusura dei buffer
            bufLaptops.close();
            bufSmartphones.close();

            // Chiusura dei file stream
            fileStreamLaptops.close();
            fileStreamSmartphones.close();
        }
        catch (IOException e){
            System.err.println("[MAGAZZINO]: Errore nell'apertura di un file.");
            e.printStackTrace();
        }

        return id_result;
    }
}
