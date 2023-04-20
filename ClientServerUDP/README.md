# Client Server con UDP socket e Proxy-Skeleton pattern 🇮🇹
## **`Client.java`**
Implementa solo la funzione **`main()`**:<br/>
    1. Crea un **riferimento** al **`CounterProxy`** → è l’unica classe con cui comunica.<br/>
    2. Crea una **variabile d’appoggio** per la ricezione delle risposte dal Proxy.<br/>
    3. Invoca i **metodi** disponibili sul Proxy, il quale implementa la stessa interfaccia **`ICounter`** del server.
## **`CounterProxy.java`**
1. Ha un attributo **`DatagramSocket`** che utilizza per mandare richieste al server (in realtà al `CounterSkeleton`).
2. Implementa un comportamento da seguire per ogni metodo dell’interfaccia **`ICounter`**, che però **non è la vera realizzazione di quella funzionalità** (es. sum() non effettua davvero la somma sul contatore remoto).
    1. Viene composto un messaggio del tipo `metodo#parmetro1#...#parametroN`.
        - Il metodo ed i parametri variano a seconda del metodo di **ICounter** che si sta implementando.
    2. Tale messaggio viene incapsulato in un `DatagramPacket request`, che prevede i campi:
        - bytes → message.getBytes()
        - length → message.getBytes().length
        - host → InetAddress.getLocalHost() o altri metodi della classe `**InetAddress**`.
        - port → (hardcoded, es. 9000)
    3. Il messaggio viene inviato sulla socket tramite `socket.send(request)`.
    4. Viene creato un **buffer a lunghezza definita** per la **ricezione della risposta**, e fornito come parametro ad un `DatagramPacket reply` in ricezione.
    5. Attesa della risposta sulla socket tramite `socket.receive(reply)`.
        - Se il metodo non è void, ma restituisce un tipo, va gestito il parsing del pacchetto ricevuto per la lettura del valore restituito.
        - A tale scopo si possono usare i metodi della classi Wrapper fornite da Java.

        ```java
        // Parsing della risposta per leggere il valore restituito dal metodo
        String replyMessage = new String(reply.getData(), 0, reply.getLength());
        x = Integer.valueOf(replyMessage).intValue();
        // [Tipo generico] --(Integer.valueof())--> [Integer]
        //                 --(.intvalue())-->       [int]
        ```


    <aside>
    💡 I sepratori <bold>`#`</bold> realizzano una <b>convenzione</b> tra il sender e il receiver, e forniscono le <b>regole di parsing</b> che il receiver dovrà implementare per analizzare e comprendere le informazioni contenute nel <b>datagramma UDP</b> ricevuto (come per i file '.csv').

    </aside>
        
## `**ICounter.java**`
    1. Interfaccia nota a tutte le classi che gestiscono la comunicazione.
    2. Definisce i metodi per operare sull’oggetto remoto “*contatore*”.
## `**CounterSkeleton.java**`
    1. Conosce i metodi dell’interfaccia **`ICounter`**, ma essendo **`abstract`**, non è necessario che ne ridefinisca i comportamenti (lo farà la sottoclasse).
        
        <aside>
        💡 Si tratta di uno **Skeleton per ereditarietà**.
        
        </aside>
        
    2. Ha un metodo **`runSkeleton()`** in cui
        1. crea una **`DatagramSocket`**;
        2. si mette in **ascolto all’infinito sullo stesso porto** su cui comunica il **CounterProxy**, implementando un meccanismo di **receive analogo** a quello del CounterProxy**;**
        3. alla **ricezione**, avvia un **thread** a cui certamente dovrà fornire `socket` e `request`, ma anche l’oggetto della sottoclasse che ha lanciato il metodo (`this`).
## **`CounterImpl.java`**
    1. **Eredita** dalla classe `CounterSkeleton`, quindi ha a disposizione tutti i meccanismi di **comunicazione** con il lato client.
    2. Ha un attributo privato che rappresenta il ***contatore***.
    3. **Implementa le funzionalità vere e proprie offerte dal server**., dunque i reali comportamenti delle funzioni definite dall’interfaccia **`ICounter`** sull’oggetto remoto *contatore*.
## `**CounterWorker.java**`
    1. Rappresenta il Thread generato dallo Skeleton all’arrivo di ogni richiesta.
        1. Eredita dalla classe **`Thread`**, dunque deve implementare il metodo **`run()`**.
    2. Riceve in ingresso come parametri una `socket`, un `**DatagramPacket** request` e un’istanza di una sottoclasse di `**CounterSkeleton`** (in questo caso `**CounterImpl**`), in quanto la prima è astratta.
    3. Nel metodo **`run()`**:
        1. divide in Token il messaggio di richiesta basandosi sulla convenzione definita (**`#`**);
            
            ```java
            StringTokenizer messageTokens = new StringTokenizer(message, "#");
            String method = messageTokens.nextToken();
            ```
            
        2. **valuta il primo Token**, che rappresenta il **metodo**, e a seconda di esso definisce dei comportamenti da seguire, come la valutazione dei token successivi;
        3. esegue l’**up-call**, cioè la chiamata a funzione alla sua istanza di Skeleton (la `CounterImpl` ricevuta come terzo parametro), che è in grado di eseguire sul server il metodo vero e proprio;
            
            ```java
            // up-call (restituisce un intero)
            int res = skeleton.increment();
            ```
            
        4. implementa un meccanismo di **generazione ed invio della risposta** al `CounterProxy`, includendo eventualmente il valore restituito dal metodo chiamato sul server.
            
            ```java
            // Creazione del messaggio/pacchetto di risposta
            String replyMessage = Integer.toString(res);
            DatagramPacket reply= new DatagramPacket(replyMessage.getBytes(),
                                                     replyMessage.getBytes().length,
                                                     request.getAddress(),
                                                     request.getPort());
            
            // Invio del messaggio/pacchetto di risposta
            try {
                socket.send(reply);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ```
## `**Server.java**`
    1. Grazie all’architettura del progetto, deve limitarsi a
        1. creare un’istanza di `**CounterImpl**`, che è per definizione un `CounterSkeleton`;
        2. eseguire il metodo **`runSkeleton()`**.
