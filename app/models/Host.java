package models;

import actions.Action;
import commands.Command;
import interfaces.HostPredefinedActionListener;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;

/**
 * Created by wasinski on 27/01/2015.
 */
@XmlSeeAlso({WindowsHost.class})
public abstract class Host extends Connectable implements HostPredefinedActionListener
{
    protected Host(){}

    /**
     * Creates a new instance of the host object.
     * @param hostName
     * @param username
     * @param password
     */
    protected Host(String hostName, String username, String password, ArrayList<Action> actions)
    {
        super(hostName, username, password, actions);
    }
}
