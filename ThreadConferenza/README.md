# Applicazione multithread in Java
## Conferenza di studenti
*La traccia dell'esercizio è reperibile al link: https://elearning.di.unipi.it/mod/assign/view.php?id=1919*

Un gruppo di n studenti organizza una conferenza. Ogni studente deve prendere la parola una volta, poi va a casa e può parlare solo uno studente alla volta.

Modellare gli studenti con i thread. Quando uno studente prende la parola, scrive il suo discorso in una variabile String. Consideriamo che il discorso è semplicemente il suo nome. Prima che un altro studente possa parlare, tutti gli studenti rimasti devono leggere il discorso dell'ultimo speaker. 

Alla fine rimane un solo studente che deve parlare. Scrive il suo nome e si spegne. Nessuno legge, l'ultimo ha parlato da solo!