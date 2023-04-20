package client;

public class Client {
    public static void main(String [] args) {

        // Riferimento al Proxy
        CounterProxy counter = new CounterProxy();

        // Variabile per la ricezione delle risposte
        int x = 0;

        // Utilizzo di setCount
        System.out.println("Set count to 10\n");
        counter.setCount("user-ACP", 10);

        // Utilizzo di sum()
        System.out.println("Sum 15 to count; ");
        x = counter.sum(15);
        System.out.println("current count: " + x + "\n");

        // Utilizzo di increment()
        System.out.println("Increment count; ");
        x = counter.increment();
        System.out.println("current count: " + x + "\n");

    }
}
