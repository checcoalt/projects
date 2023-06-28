package manager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import service.IObserver;

public class ManagerProxy implements IObserver {

    private int port;

    public ManagerProxy(int port){
        this.port = port;
    }

    public void notifyAlert(int criticality) {

        try {

            Socket socket = new Socket(InetAddress.getLocalHost(), this.port);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(criticality);
            out.flush();

            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
