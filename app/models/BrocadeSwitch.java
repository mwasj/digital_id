package models;

import actions.Action;
import command_sets.BrocadeCommandSet;
import commands.Command;
import context.BrocadeSwitchContext;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
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

    /**
     * Creates a new instance of BrocadeSwitch object.
     * @param switchName - host name of this switch.
     * @param username - username required to log in.
     * @param password - password required to log in.
     */
    public BrocadeSwitch(String switchName, String username, String password, ArrayList<Action> actions)
    {
        super(switchName, username, password, actions);
    }
}
