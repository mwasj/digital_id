package models;

import actions.Action;
import command_sets.CiscoCommandSet;
import commands.Command;
import context.CiscoSwitchContext;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

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
     */
    public CiscoSwitch(String switchName, String username, String password, ArrayList<Action> actions)
    {
        super(switchName, username, password, actions);
    }

    /**
     * Empty constructor required by JAXB.
     */
    public CiscoSwitch(){
        setContext(new CiscoSwitchContext(new CiscoCommandSet()));
    }

}