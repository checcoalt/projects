package client;

import dispatcher.IDispatcher;

public class ClientThread extends Thread {

    private IDispatcher dispatcher;
    private final int N_REQUESTS;

    public ClientThread(IDispatcher dispatcher, int requests){
        this.dispatcher = dispatcher;
        this.N_REQUESTS = requests;
    }

    public void run() {

        int x;

        for (int i = 0; i < N_REQUESTS; i++){
            x = (int)(Math.random()*4);
		    System.out.println ("[CLN] invio comando # " + x );
		    dispatcher.sendCmd(x);
        }
    }
    
}
