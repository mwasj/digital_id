package commands;

import com.jcraft.jsch.JSchException;
import core.CommandResponse;
import core.ConnectionManager;
import models.RemoteCommandType;

import java.io.IOException;

/**
 * Created by wasinski on 18/05/2015.
 */
public class SendRemoteCommand extends Command {

    private String commandString;
    private RemoteCommandType remoteCommandType;

    public RemoteCommandType getRemoteCommandType() {
        return remoteCommandType;
    }

    public String getCommandString() {
        return commandString;
    }

    public SendRemoteCommand(String commandString, RemoteCommandType remoteCommandType, int waitFor, boolean comparable, boolean causesWebUpdate)
    {
        super(CommandType.UserDefined, waitFor, comparable, causesWebUpdate);

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
