package ThreadConferenza;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Studente extends Thread {

    private String nome;                    // nome identificativo dello studente (thread) che ha la parola (accesso alla variabile)
    private static String discorso;         // riferimento alla variabile condivisa

    private static int N_ATTIVI = 0;        // Thread attivi al momento
    private static int N_LETTORI = 0;       // Thread lettori in attesa sul lock

    private static final Lock lock = new ReentrantLock();                   // lock
    private static final Condition R_CONDITION = lock.newCondition();       // variabile condition per i lettori
    
    public Studente(String nome, String d){
        this.nome = nome;
        discorso = d;
        N_ATTIVI++;
    }

    public void run(){

        try {

            lock.lock();
            // se il Thread si trova qui, è l'unico ad avere accesso al discorso

            // Prima di cominciare, si attende che arrivino tutti
            if (N_LETTORI < App.N_STUDENTI-1) {
                N_LETTORI++;
                R_CONDITION.await();
            }

/*
    Questa variabile condition è valutata con un if, in quanto solo per il primo discorso si dovrà attendere l'arrivo di tutti gli studenti.
    Su questa condition si arrestano tutti i thread eccetto uno, l'ultimo ad arrivare prima che la conferenza possa iniziare.
    I thread che si arrestano in questa sezione, saranno risvegliati dalla signalAll() al termine del discorso del primo thread.
    In quella situazione, certamente saranno arrivati tutti: non è necessario ripetere la verifica.
*/ 

            // Fase di sincronizzazione sulla lettura da parte dei thread rimasti
            if (N_ATTIVI < App.N_STUDENTI){         // Se non sono il primo a parlare (questa condizione è falsa solo per il primo)
                while(N_LETTORI < N_ATTIVI) {       // Tutti devono leggere
                    
                    // Lettura
                    N_LETTORI++;
                    System.out.println(this.nome + " ha letto il discorso di: " + discorso);

                    // Come proseguire
                    if (N_LETTORI == N_ATTIVI) break;   // se hanno già letto tutti gli altri rimanenti, non mi arresto
                    else R_CONDITION.await();           // altrimenti mi arresto

                }
            }

            // se il Thread si trova qui, tutti i Thread rimasti hanno letto il discorso dell'ultimo studente che ha parlato

            // se rimane qualcuno ad ascoltare, posso parlare
            if (N_ATTIVI != 1) {
            
                discorso = this.nome;
                System.out.println("\n" + discorso + " sta parlando...");
                System.out.println(this.nome + " ha terminato il suo turno.\n");
            
            }
            else {
                System.out.println("\nUffa! Sono rimasto solo...");
                System.out.println(this.nome + " ha terminato il suo turno.\n");
            }

            N_ATTIVI--;                 // questo studente sta andando via

            // tutti pronti per venire a leggere
            N_LETTORI = 0;
            R_CONDITION.signalAll();

            // ora può lasciare la parola al prossimo Thread
            lock.unlock();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
}
