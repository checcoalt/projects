package subscriber;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import service.IObserver;

public class SubscriberSkeleton {

    private ServerSocket server;
    private int port;
    private IObserver observer;

    public SubscriberSkeleton(IObserver observer, int port) {
        this.observer = observer;
        this.port = port;
    }

    public void runSkeleton() {

        try {
            server = new ServerSocket(port);

            while (true) {

                Socket socket = server.accept();

                DataInputStream in = new DataInputStream(socket.getInputStream());
                int criticality = in.readInt();
                observer.notifyAlert(criticality);

                in.close();
                socket.close();

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
