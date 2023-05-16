package server;

import service.IMagazzino;

public class MagazzinoServer {
    
    public static void main(String[] args) {

        /*
         * Da terminale in C:\PATH\src\bin:
         *      > java server.MagazzinoServer PORT
         *  es. > java server.MagazzinoServer 8000
         */
        
        if (args.length < 1){
            System.err.println("[MAGAZZINO-SERVER]: numero di argomenti insufficiente: PORT_NUMBER richiesto.");
            System.exit(1);
        }

        else {

            // Lettura del numero di porto da CLI
            final int PORT = Integer.valueOf(args[0]);

            // Istanza del magazzino (con implementazione dei metodi)
            IMagazzino magazzino = new MagazzinoImpl();

            // Istanza dello Skeleton, che sfrutta le implementazioni di MagazzinoImpl
            MagazzinoSkeleton skeleton = new MagazzinoSkeleton(PORT, magazzino);

            System.out.println("[MAGAZZINO-SERVER]: Avviato correttamente sul porto " + PORT);

            // Avvia il server
            skeleton.runSkeleton();
        }

    }

}
