package models;

import commands.Command;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;

/**
 * Created by wasinski on 05/02/2015.
 */
@XmlSeeAlso({CiscoSwitch.class, BrocadeSwitch.class})
public abstract class Switch extends Connectable
{
    protected Switch()
    {

    }

    protected Switch(String hostName, String userName, String password, ArrayList<Command> commands)
    {
        super(hostName,userName,password, commands);
    }
}


