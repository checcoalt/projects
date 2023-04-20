package server;

public class CounterImpl extends CounterSkeleton {
    private int count;

    public void setCount(String id, int s){
        System.out.println("    [CounterImpl]: count set to " + s + " by client " + id + ".\n");
        count = s;
    }

    public int sum(int s){
        System.out.println("    [CounterImpl]: adding " + s + " to count.\n");
        count = count + s;
        return count;
    }

    public int increment(){
        System.out.println("    [CounterImpl]: adding 1 to count.\n");
        count = count + 1;
        return count;
    }
}

// Vanno implementate in questa classe eventuali politiche di sincronizzazione.
// Ad esempio, i metodi implementati qui vanno definiti synchronized.
