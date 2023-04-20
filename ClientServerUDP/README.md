## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

# Spiegazione
1. **`Client.java`**
    1. Implementa solo la funzione `**main()**`:
        1. Crea un **riferimento** al **`CounterProxy`** → è l’unica classe con cui comunica.
        2. Crea una **variabile d’appoggio** per la ricezione delle risposte dal Proxy.
        3. Invoca i **metodi** disponibili sul Proxy, il quale implementa la stessa interfaccia **`ICounter`** del server.
2. **`CounterProxy.java`**
    1. Ha un attributo **`DatagramSocket`** che utilizza per mandare richieste al server (in realtà al `CounterSkeleton`).
    2. Implementa un comportamento da seguire per ogni metodo dell’interfaccia **`ICounter`**, che però **non è la vera realizzazione di quella funzionalità** (es. sum() non somma veramente).
        1. Viene composto un messaggio del tipo `metodo**#**parmetro1**#**...**#**parametroN`.
            - Il metodo ed i parametri variano a seconda del metodo di **ICounter** che si sta implementando.
        2. Tale messaggio viene incapsulato in un `**DatagramPacket** request`, che prevede i campi:
            - bytes → message.getBytes()
            - length → message.getBytes().length
            - host → InetAddress.getLocalHost() o altri metodi
            - port → (hardcoded, es. 9000)
        3. Il messaggio viene inviato sulla socket tramite `socket.**send**(request)`.
        4. Viene creato un **buffer a lunghezza definita** per la **ricezione della risposta**, e fornito come parametro ad un `**DatagramPacket** reply` in ricezione.
        5. Attesa della risposta sulla socket tramite `socket.**receive**(reply)`.
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
        💡 I sepratori **`#`** realizzano una **convenzione** tra il sender e il receiver, e forniscono le **regole di parsing** che il receiver dovrà implementare per analizzare e comprendere le informazioni contenute nel **datagramma UDP** ricevuto (come per i file `.csv`).
        
        </aside>
        
3. `**CounterSkeleton.java**`
    1. Conosce i metodi dell’interfaccia **`ICounter`**, ma essendo **`abstract`**, non è necessario che ne ridefinisca i comportamenti (lo farà la sottoclasse).
        
        <aside>
        💡 Si tratta di uno **Skeleton per ereditarietà**.
        
        </aside>
        
    2. Ha un metodo **`runSkeleton()`** in cui
        1. crea una **`DatagramSocket`**;
        2. si mette in **ascolto all’infinito sullo stesso porto** su cui comunica il **CounterProxy**, implementando un meccanismo di **receive analogo** a quello del CounterProxy**;**
        3. alla **ricezione**, avvia un **thread** a cui certamente dovrà fornire `socket` e `request`, ma anche l’oggetto della sottoclasse che ha lanciato il metodo (`this`).