package commands;

import com.google.gson.annotations.Expose;
import com.jcraft.jsch.JSchException;
import core.CommandResponse;
import core.ConnectionManager;
import models.RemoteCommandType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;

/**
 * Created by wasinski on 18/05/2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SendRemoteCommand extends Command
{
    @Expose
    private String commandString;
    @Expose
    private RemoteCommandType remoteCommandType;

    public RemoteCommandType getRemoteCommandType() {
        return remoteCommandType;
    }

    public String getCommandString() {
        return commandString;
    }

    public SendRemoteCommand(int id, String displayName, String commandString, RemoteCommandType remoteCommandType, int waitFor, boolean comparable)
    {
        super(id, displayName, waitFor, comparable);

        this.commandString = commandString;
        this.remoteCommandType = remoteCommandType;
    }

    public SendRemoteCommand()
    {

    }

    @Override
    public void initialise(ConnectionManager connectionManager)
    {
        setConnectionManager(connectionManager);
    }

    @Override
    public CommandResponse execute()
    {
        if(getConnectionManager() == null)
        {
            System.out.println("Connection Manager was not initialised. This command will not be executed");
            return null;
        }

        try
        {
            setCommandResponse(getConnectionManager().sendCommand(commandString, remoteCommandType));
        } catch (IOException | JSchException e)
        {
            e.printStackTrace();
        }
        return getCommandResponse();
    }
}
