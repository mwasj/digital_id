package commands;

import core.CommandResponse;
import core.ConnectionManager;

/**
 * Created by wasinski on 15/05/2015.
 */
public class SendFileCommand extends Command
{
    private String localFile;
    private String remoteFile;

    public SendFileCommand(String localFile, String remoteFile, int id)
    {
        super(CommandType.Predefined, 0, true, true);
        this.localFile = localFile;
        this.remoteFile = remoteFile;
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

        setCommandResponse(getConnectionManager().sendFile(localFile, remoteFile));
        return getCommandResponse();
    }
}
