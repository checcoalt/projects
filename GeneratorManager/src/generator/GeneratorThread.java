package generator;

import java.rmi.RemoteException;
import java.util.Random;

import service.AlertNotification;
import service.IManager;

public class GeneratorThread extends Thread {

    private IManager manager;
    private int N_REQUESTS;

    public GeneratorThread(IManager manager, int N_REQUESTS){
        this.manager = manager;
        this.N_REQUESTS = N_REQUESTS;
    }

    public void run(){

        try {
            for(int i = 0; i < N_REQUESTS; i++){
            
                Random random = new Random();
                int componentID = random.nextInt(1, 6);
                int criticality = random.nextInt(1, 4);

                AlertNotification alert = new AlertNotification(componentID, criticality);

                manager.sendNotification(alert);
                System.out.println("[GENERATOR-THREAD]: new notification sent:\n\t\t" + alert.toString());
            }
        }
        catch (RemoteException e){
            e.printStackTrace();
        }

    }
    
}
