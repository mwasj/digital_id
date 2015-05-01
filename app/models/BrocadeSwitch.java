package models;

import com.jcraft.jsch.JSchException;
import command_sets.BrocadeCommandSet;
import context.BrocadeSwitchContext;
import core.CommandResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wasinski on 19/02/2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BrocadeSwitch extends Switch {

    public BrocadeSwitch()
    {
        {
            setContext(new BrocadeSwitchContext(new BrocadeCommandSet()));
        }
    }

    @Override
    public void runCommands() {

    }

    /**
     * Creates a new instance of BrocadeSwitch object.
     * @param switchName - host name of this switch.
     * @param username - username required to log in.
     * @param password - password required to log in.
     * @param activePorts - list of active ports for this configuration.
     */
    public BrocadeSwitch(String switchName, String username, String password, ArrayList<SwitchPort> activePorts, ArrayList<CommandResponse> commands)
    {
        super(switchName, username, password, activePorts, commands);
    }

    @Override
    public CommandResponse setPortSpeed(SwitchPort switchPort, int newSpeed) throws IOException, JSchException {
        return null;
    }

    @Override
    public int getPortSpeed(String portName) {
        return 0;
    }

    @Override
    public int getPortSpeed(SwitchPort switchPort) {
        return 0;
    }
}
