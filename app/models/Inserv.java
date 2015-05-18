package models;

import actions.Action;
import com.jcraft.jsch.JSchException;
import commands.Command;
import context.InservContext;
import core.CommandResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Michal on 13/12/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Inserv extends Connectable
{
    public Inserv()
    {
        setContext(new InservContext());
    }

    private String inservName;



    public Inserv(String hostName, String userName, String password, ArrayList<Action> actions) {
        super(hostName, userName, password, actions);
    }

    public String getInservName() {
        return inservName;
    }

}
