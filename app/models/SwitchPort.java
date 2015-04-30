package models;

import context.Context;
import context.SwitchPortContext;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SwitchPort
{
    private String portNumber;
    @XmlTransient
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setPortInformation(String portInformation) {
        this.portInformation = portInformation;
    }

    private String portInformation;

    private int speed;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public String getPortInformation() {
        return portInformation;
    }

    public SwitchPort(String portNumber)
    {
        this.portNumber = portNumber;
        this.context = new SwitchPortContext();
    }

    public SwitchPort(){
        context = new SwitchPortContext();
    }
}
