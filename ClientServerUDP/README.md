# Client-Server con UDP socket e Proxy-Skeleton pattern 🇮🇹
[Class Diagram](ClassDiagram.png)
## **`Client.java`**
Implementa solo la funzione **`main()`**:
1. crea un **riferimento** al **`CounterProxy`** → è l’unica classe con cui comunica;
2. crea una **variabile d’appoggio** per la ricezione delle risposte dal Proxy;
3. invoca i **metodi** disponibili sul Proxy, il quale implementa la stessa interfaccia **`ICounter`** del server.
## **`CounterProxy.java`**
1. Ha un attributo **`DatagramSocket`** che utilizza per mandare richieste al server (in realtà al `CounterSkeleton`).
2. Implementa un comportamento da seguire per ogni metodo dell’interfaccia **`ICounter`**, che però **non è la vera realizzazione di quella funzionalità** (es. sum() non effettua davvero la somma sul contatore remoto).
    1. Viene composto un messaggio del tipo `metodo#parmetro1#...#parametroN`.
        - Il metodo ed i parametri variano a seconda del metodo di **ICounter** che si sta implementando.
    2. Tale messaggio viene incapsulato in un `DatagramPacket request`, che prevede i campi:
        - bytes → message.getBytes()
        - length → message.getBytes().length
        - host → InetAddress.getLocalHost() o altri metodi della classe **`InetAddress`**.
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
        // [Tipo generico]     --(Integer.valueof())-->     [Integer]    --(.intvalue())-->       [int]
        ```


    <aside>
    💡 I sepratori <b>#</b> realizzano una <b>convenzione</b> tra il sender e il receiver, e forniscono le <b>regole di parsing</b> che il receiver dovrà implementare per analizzare e comprendere le informazioni contenute nel <b>datagramma UDP</b> ricevuto (come per i file <b>.csv</b>).

    </aside>
        
## **`ICounter.java`**
1. Interfaccia nota a tutte le classi che gestiscono la comunicazione.
2. Definisce i metodi per operare sull’oggetto remoto “*contatore*”.
## **`CounterSkeleton.java`**
1. Conosce i metodi dell’interfaccia **`ICounter`**, ma essendo **`abstract`**, non è necessario che ne ridefinisca i comportamenti (lo farà la sottoclasse).

    <aside>
    💡 Si tratta di uno <b>Skeleton per ereditarietà</b>.

    </aside>

2. Ha un metodo **`runSkeleton()`** in cui
    1. crea una **`DatagramSocket`**;
    2. si mette in **ascolto all’infinito sullo stesso porto** su cui comunica il **CounterProxy**, implementando un meccanismo di **receive analogo** a quello del CounterProxy;
    3. alla **ricezione**, avvia un **thread** a cui certamente dovrà fornire `socket` e `request`, ma anche l’oggetto della sottoclasse che ha lanciato il metodo (`this`).
## **`CounterImpl.java`**
1. **Eredita** dalla classe `CounterSkeleton`, quindi ha a disposizione tutti i meccanismi di **comunicazione** con il lato client.
2. Ha un attributo privato che rappresenta il ***contatore***.
3. **Implementa le funzionalità vere e proprie offerte dal server**., dunque i reali comportamenti delle funzioni definite dall’interfaccia **`ICounter`** sull’oggetto remoto *contatore*.
## **`CounterWorker.java`**
1. Rappresenta il Thread generato dallo Skeleton all’arrivo di ogni richiesta.
    1. Eredita dalla classe **`Thread`**, dunque deve implementare il metodo **`run()`**.
2. Riceve in ingresso come parametri una `socket`, un `DatagramPacket request` e un’istanza di una sottoclasse di **`CounterSkeleton`** (in questo caso **`CounterImpl`**), in quanto la prima è astratta.
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
## **`Server.java`**
Grazie all’architettura del progetto, deve limitarsi a
1. creare un’istanza di **`CounterImpl`**, che è per definizione un `CounterSkeleton`;
2. eseguire il metodo **`runSkeleton()`**.

# Client-Server with UDP socket and Proxy-Skeleton pattern 🇺🇸
[Class Diagram](ClassDiagram.png)

## **`Client.java`**

Implements only the **`main()`** function:

1. creates a **reference** to the **`CounterProxy`** → it is the only class it communicates with;
2. creates a **temporary variable** to receive responses from the Proxy;
3. invokes the **methods** available on the Proxy, which implements the same **`ICounter`** interface as the server.

## **`CounterProxy.java`**

1. Has a **`DatagramSocket`** attribute that it uses to send requests to the server (actually to the `CounterSkeleton`).
2. Implements a behavior to follow for each method of the **`ICounter`** interface, but **it is not the real realization of that functionality** (e.g. sum() does not actually perform the sum on the remote counter).
    1. A message of the type `method#parameter1#...#parameterN` is composed.
        - The method and parameters vary depending on the method of **ICounter** being implemented.
    2. This message is encapsulated in a `DatagramPacket request`, which has the fields:
        - bytes → message.getBytes()
        - length → message.getBytes().length
        - host → InetAddress.getLocalHost() or other methods of the **`InetAddress`** class.
        - port → (hardcoded, e.g. 9000)
    3. The message is sent on the socket via `socket.send(request)`.
    4. A **fixed-length buffer** is created for **receiving the response**, and provided as a parameter to a receiving `DatagramPacket reply`.
    5. Wait for the response on the socket via `socket.receive(reply)`.
        - If the method is not void, but returns a type, the received packet must be parsed to read the returned value.
        - To do this, you can use the methods of the Java Wrapper classes.
        
        ```java
        // Parsing the response to read the value returned by the method
        String replyMessage = new String(reply.getData(), 0, reply.getLength());
        x = Integer.valueOf(replyMessage).intValue();
        // [Generic type]     --(Integer.valueof())-->     [Integer]    --(.intvalue())-->       [int]
        ```
        
    
    <aside>
    💡 The <b>#</b> separators implement a <b>convention</b> between the sender and the receiver, and provide the <b>parsing rules</b> that the receiver must implement to analyze and understand the information contained in the received <b>UDP datagram</b> (as for <b>.csv</b> files).
    
    </aside>
    

## **`ICounter.java`**

1. Interface known to all communication-handling classes.
2. Defines the methods for operating on the remote object "*counter*".

## **`CounterSkeleton.java`**

1. Knows the methods of the **`ICounter`** interface, but being **`abstract`**, it is not necessary to redefine their behaviors (this will be done by the subclass).
    
    <aside>
    💡 This is a <b>Skeleton for inheritance</b>.
    
    </aside>
    
2. Has a **`runSkeleton()`** method in which
    1. creates a **`DatagramSocket`**;
    2. listens infinitely on the same port on which the **CounterProxy** communicates, implementing a **similar receive mechanism** to that of the CounterProxy;
    3. on **reception**, it starts a **thread** to which it must surely provide `socket` and `request`, but also the object of the subclass that launched the method (`this`).

## **`CounterImpl.java`**

1. **Inherits** from the `CounterSkeleton` class, therefore it has all the **communication mechanisms** with the client side available.
2. Has a private attribute that represents the ***counter***.
3. **Implements the actual functionalities offered by the server**., therefore the real behaviors of the functions defined by the **`ICounter`** interface on the remote *counter* object.

## **`CounterWorker.java`**

1. Represents the Thread generated by the Skeleton upon arrival of each request.
    1. Inherits from the **`Thread`** class, therefore it must implement the **`run()`** method.
2. Receives a `socket`, a `DatagramPacket request`, and an instance of a subclass of **`CounterSkeleton`** (in this case **`CounterImpl`**) as parameters, as the first one is abstract.
3. In the **`run()`** method:
    1. divides the request message into Tokens based on the defined convention (**`#`**);
        
        ```java
        StringTokenizer messageTokens = new StringTokenizer(message, "#");
        String method = messageTokens.nextToken();
        ```
        
    2. **evaluates the first Token**, which represents the **method**, and depending on it, defines behaviors to follow, such as evaluating the subsequent tokens;
    3. performs the **up-call**, i.e., the function call to its Skeleton instance (the `CounterImpl` received as the third parameter), which is able to execute the actual method on the server;
        
        ```java
        // up-call (returns an integer)
        int res = skeleton.increment();
        
        ```
        
    4. implements a mechanism of **generation and sending of the response** to the `CounterProxy`, possibly including the value returned by the method called on the server.
        
        ```java
        // Creation of the reply message/packet
        String replyMessage = Integer.toString(res);
        DatagramPacket reply= new DatagramPacket(replyMessage.getBytes(),
                                                 replyMessage.getBytes().length,
                                                 request.getAddress(),
                                                 request.getPort());
        
        // Sending of the reply message/packet
        try {
            socket.send(reply);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ```
        

## **`Server.java`**

Thanks to the project architecture, it must be limited to:

1. creating an instance of **`CounterImpl`**, which is by definition a `CounterSkeleton`;
2. execute the **`runSkeleton()`** method.
