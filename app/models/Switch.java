package models;

import com.jcraft.jsch.JSchException;
import context.Context;
import core.CommandResponse;
import core.SwitchHelper;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wasinski on 05/02/2015.
 */
@XmlSeeAlso({CiscoSwitch.class, BrocadeSwitch.class})
public abstract class Switch extends Connectable
{
    public ArrayList<SwitchPort> getActivePorts() {
        return activePorts;
    }

    public void setActivePorts(ArrayList<SwitchPort> activePorts) {
        this.activePorts = activePorts;
    }

    private ArrayList<SwitchPort> activePorts;

    private String switchInformation;

    private String flogiDatabase;

    public abstract CommandResponse setPortSpeed(SwitchPort switchPort, int newSpeed) throws IOException, JSchException;

    public abstract int getPortSpeed(String portName);

    public abstract int getPortSpeed(SwitchPort switchPort);

    public String getFlogiDatabase() {
        return flogiDatabase;
    }

    public void setFlogiDatabase(String flogiDatabase) {
        this.flogiDatabase = flogiDatabase;
    }

    public void setSwitchInformation(String switchInformation) {
        this.switchInformation = switchInformation;
    }

    public String getSwitchInformation() {
        return switchInformation;

    }

    protected Switch()
    {

    }

    protected Switch(String hostName, String userName, String password, ArrayList<SwitchPort> activePorts, ArrayList<CommandResponse> commands)
    {
        super(hostName,userName,password, commands);
        this.activePorts = activePorts;
    }

    public void retrieveSwitchInformation() throws IOException, JSchException
    {
        setSwitchInformation(SwitchHelper.getSwitchInformation(this));
    }

    public void retrieveFlogiDatabase() throws IOException, JSchException
    {
        setFlogiDatabase(SwitchHelper.getFlogiDatabase(this));
    }

    public void retrievePortInformation() throws IOException, JSchException
    {
        if(getActivePorts() != null)
        {
            for(SwitchPort switchPort : getActivePorts())
            {
                switchPort.setPortInformation(SwitchHelper.getPortInformatiuon(this, switchPort.getPortNumber()));
            }
        }
    }
}


