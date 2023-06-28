package generator;

import java.rmi.RemoteException;
import java.util.Random;

import service.IDispatcher;
import service.Reading;

public class GeneratorThread extends Thread {

    private final IDispatcher dispatcher;
    private final int NUM_INVOKE;


    public GeneratorThread(IDispatcher dispatcher, int numinvoke) {
        this.dispatcher = dispatcher;
        this.NUM_INVOKE = numinvoke;
    }

    public void run() {

        try {
            for (int i = 0; i < NUM_INVOKE; i++) {

                Random random = new Random();

                String tipo = random.nextInt(1, 3) == 1 ? "temperatura" : "pressione";
                int valore = random.nextInt(0, 51);

                Thread.sleep(5000);

                Reading r = new Reading(tipo, valore);
                dispatcher.setReading(r);

                System.out.println("[GENERATOR-THREAD]: new value pushed on dispatcher:\n\t\t" + r.toString());

            }
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }
    
}
