package models;

import com.jcraft.jsch.JSchException;
import context.Context;
import core.CommandResponse;
import core.DigitalID;
import interfaces.ConnectableInterface;
import interfaces.WebUpdater;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michal on 08/02/15.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Host.class, Switch.class, Inserv.class})
public abstract class Connectable
{
    @XmlTransient
    private ConnectionManager connectionManager;

    public ConnectionManager getConnectionManager()
    {
        return connectionManager;
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

    public ArrayList<Instruction> getCommandsToRun() {
        return commandsToRun;
    }

    @XmlTransient
    public ArrayList<Instruction> commandsToRun;

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
        this.commandsToRun = new ArrayList<>();
        connectionManager = new ConnectionManager(this);
        this.commands = commands;
    }

    protected Connectable()
    {

    }

    public void connect() throws IOException, JSchException
    {
        connectionManager.connect();
    }

    public abstract void runCommands();

    public void updateWebInterface(CommandResponse commandResponse)
    {
        if(webUpdater != null)
            webUpdater.update(commandResponse);
        else
            System.out.println("Web updater == null, please set it first.");
    }

    public void disconnect()
    {
        connectionManager.close();
    }
}
