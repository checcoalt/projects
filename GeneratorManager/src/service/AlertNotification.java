package service;

import java.io.Serializable;

public class AlertNotification implements Serializable {

    private int componentID;
    private int criticality;

    public AlertNotification(int componentID, int criticality) {
        this.componentID = componentID;
        this.criticality = criticality;
    }

    public int getComponentID() {
        return componentID;
    }
    public void setComponentID(int componentID) {
        this.componentID = componentID;
    }
    public int getCriticality() {
        return criticality;
    }
    public void setCriticality(int criticality) {
        this.criticality = criticality;
    }

    @Override
    public String toString() {
        return "AlertNotification [componentID=" + componentID + ", criticality=" + criticality + "]";
    }

    
}
