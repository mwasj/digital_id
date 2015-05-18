package models;

import command_sets.WindowsCommandSet;
import commands.Command;
import context.WindowsContext;
import core.CommandResponse;
import predefined_actions.Sg3Utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by wasinski on 04/02/2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WindowsHost extends Host {

    /**
     * Creates a new instance of the host object.
     *
     * @param hostName
     * @param username
     * @param password
     */
    public WindowsHost(String hostName, String username, String password, ArrayList<Command> commands) {
        super(hostName, username, password, commands);
    }

    /**
     * Empty constructor required by JAXB.
     */
    public WindowsHost()
    {
        setContext(new WindowsContext(new WindowsCommandSet()));
    }


    @Override
    public CommandResponse runSg3Utils()
    {
        return Sg3Utils.getWindowsReport(this);
    }
}
