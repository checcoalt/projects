package service;

import java.io.Serializable;
import java.rmi.RemoteException;

// Passare oggetti con RMI --> Serializable
// Esportazione di stub --> UnicastRemoteObject (nell'implementazione)
// Esportare interfaccia --> Remote

public class Reading implements Serializable {

    private String tipo;
    private int valore;

    public Reading(String tipo, int valore) throws RemoteException {
        this.tipo = tipo;
        this.valore = valore;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getValore() {
        return valore;
    }
    public void setValore(int valore) {
        this.valore = valore;
    }
    
    @Override
    public String toString() {
        return "Reading [tipo=" + tipo + ", valore=" + valore + "]";
    }

}