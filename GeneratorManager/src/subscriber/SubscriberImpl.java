package subscriber;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import service.IObserver;

public class SubscriberImpl implements IObserver {
    
    private String filename;

    public SubscriberImpl(String filename) {
        this.filename = filename;
    }

    public void notifyAlert(int criticality) {

        try {
            FileOutputStream out = new FileOutputStream(filename, true);
            PrintWriter pw = new PrintWriter(out);
            pw.println("Recived notification with criticality " + criticality);
            System.out.println("Recived notification with criticality " + criticality);
            pw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
