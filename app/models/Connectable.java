package models;

import commands.Command;
import context.Context;
import interfaces.WebUpdater;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Created by Michal on 08/02/15.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Host.class, Switch.class, Inserv.class})
public abstract class Connectable
{
    public void setCommands(ArrayList<Command> commands)
    {
        this.commands = commands;
    }

    public String getHostName() {
        return hostName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    private ArrayList<Command> commands;

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @XmlTransient
    private WebUpdater webUpdater;

    @XmlTransient
    private Context context;

    public Context getContext()
    {
        return context;
    }

    private String hostName;

    public void setWebUpdater(WebUpdater webUpdater) {
        this.webUpdater = webUpdater;
    }

    public WebUpdater getWebUpdater() {
        return webUpdater;
    }

    @XmlTransient
    private String userName;
    @XmlTransient
    private String password;

    protected Connectable(String hostName, String userName, String password, ArrayList<Command> commands)
    {
        this.hostName = hostName;
        this.userName = userName;
        this.password = password;
        this.commands = commands;
    }

    protected Connectable()
    {

    }

    public void updateWebInterface(WebUpdate webUpdate)
    {
        if(webUpdater != null)
            webUpdater.sendUpdate(webUpdate);
        else
            System.out.println("Web updater == null, please set it first.");
    }

    public int hashCode()
    {
        return this.getHostName().hashCode();
    }

    public boolean equals(Object object)
    {
        if(object instanceof Connectable)
        {
            Connectable c = (Connectable) object;
            return c.hostName.equals(this.hostName);
        }
        else
        {
            return false;
        }
    }
}
