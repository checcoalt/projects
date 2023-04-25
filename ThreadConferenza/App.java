package ThreadConferenza;

public class App {

    public static int N_STUDENTI = 5;   // numero di Threads
    public static String discorso = ""; // variabile condivisa su cui ogni Thread scriverà

    public static void main(String[] args) {
        
        Studente [] studenti = new Studente[N_STUDENTI];

        // Creazione e avvio dei Threads
        for (int i = 0; i < N_STUDENTI; i++){
            studenti[i] = new Studente(("Studente " + i), discorso);
            studenti[i].start();
        }

        // Attesa della terminazione di tutti i Threads
        for (int i = 0; i < N_STUDENTI; i++){
            try {
                studenti[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Conferenza terminata.");

    }

}
