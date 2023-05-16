package service;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.Remote;

/*
 * Contiene informazioni sulla locazione in rete (indirizzo ip e port number)
 * nel contesto del protocollo TCP/IP, ed i relativi metodi di utilità e costruttore di copia.
 */

public class TCP_IP_DATA implements Remote, Serializable {
    
    // Informations about net location
    private InetAddress ipAddress;
    private int PORT;

    // Constructor
    public TCP_IP_DATA(InetAddress address, int port) {
        this.ipAddress = address;
        this.PORT = port;
    }

    // Copy-Constructor
    public TCP_IP_DATA(TCP_IP_DATA netData) {
        this.ipAddress = netData.getIpAddress();
        this.PORT = netData.getPORT();
    }

    // Setters
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPORT() {
        return PORT;
    }

}
