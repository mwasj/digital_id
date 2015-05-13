package models;

import com.jcraft.jsch.JSchException;
import context.Context;
import core.CommandResponse;
import core.CommandRunner;
import interfaces.WebUpdater;

import javax.xml.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Michal on 08/02/15.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Host.class, Switch.class, Inserv.class})
public abstract class Connectable
{
    public void setCommandResponses(ArrayList<CommandResponse> commandResponses) {
        this.commandResponses = commandResponses;
    }

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

    private ArrayList<CommandResponse> commandResponses;

    public ArrayList<CommandResponse> getCommandResponses() {
        return commandResponses;
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

    protected Connectable(String hostName, String userName, String password, ArrayList<CommandResponse> commandResponses)
    {
        this.hostName = hostName;
        this.userName = userName;
        this.password = password;
        connectionManager = new ConnectionManager(this);
        this.commandResponses = commandResponses;
    }

    protected Connectable()
    {

    }

    public void connect() throws IOException, JSchException
    {
        connectionManager.connect();
    }

    public void runCommands()
    {
        CommandRunner commandRunner = new CommandRunner(this);
        this.commandResponses = commandRunner.execute();
    }

    public void updateWebInterface(WebUpdate webUpdate)
    {
        if(webUpdater != null)
            webUpdater.sendUpdate(webUpdate);
        else
            System.out.println("Web updater == null, please set it first.");
    }

    public void disconnect()
    {
        connectionManager.close();
    }
}
