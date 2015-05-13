package models;

import com.jcraft.jsch.JSchException;
import command_sets.CiscoCommandSet;
import context.CiscoSwitchContext;
import core.CommandResponse;
import core.CommandResponseCode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Represents a Cisco Switch object.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CiscoSwitch extends Switch
{
    /**
     * Creates a new instance of CiscoSwitch object.
     * @param switchName - host name of this switch.
     * @param username - username required to log in.
     * @param password - password required to log in.
     * @param activePorts - list of active ports for this configuration.
     */
    public CiscoSwitch(String switchName, String username, String password, ArrayList<SwitchPort> activePorts, ArrayList<CommandResponse> commands)
    {
        super(switchName, username, password, activePorts, commands);
    }

    @Override
    public CommandResponse setPortSpeed(SwitchPort switchPort, int newSpeed) throws IOException, JSchException
    {
        return null;
    }

    @Override
    public int getPortSpeed(String portName)
    {
        for(SwitchPort switchPort : getActivePorts())
        {
            if(portName.equals(switchPort.getPortNumber()))
            {
                return switchPort.getSpeed();
            }
        }

        return 0;
    }

    @Override
    public int getPortSpeed(SwitchPort switchPort)
    {
        for(SwitchPort switchPort1 : getActivePorts())
        {
            if(switchPort == switchPort1)
            {
                return switchPort.getSpeed();
            }
        }

        return 0;
    }

    /**
     * Empty constructor required by JAXB.
     */
    public CiscoSwitch(){
        setContext(new CiscoSwitchContext(new CiscoCommandSet()));
    }

}