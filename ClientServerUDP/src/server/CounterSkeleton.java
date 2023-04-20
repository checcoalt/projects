package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import service.ICounter;

public abstract class CounterSkeleton implements ICounter {
    
    public void runSkeleton(){

        try {

            DatagramSocket socket = new DatagramSocket(9000);   // comunicazione sullo stesso porto della socket usata dal Proxy

            while (true) {
                // Stesso meccanismo di receive bloccante del Proxy
                byte[] buffer = new byte[100];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                // Avvio del thread worker
                CounterWorker worker = new CounterWorker(socket, request, this); // abstract --> il this riferirà un oggetto di una sottoclasse
                worker.start();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
