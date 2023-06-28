package disk;

import javax.jms.Message;
import javax.jms.MessageListener;


public class DiskListener implements MessageListener {
    
    public void onMessage(Message m){

        DiskThread thread = new DiskThread(m);
        thread.start();

    }


}
