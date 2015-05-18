package commands;

import core.CommandResponse;
import core.ConnectionManager;

/**
 * Created by Michal on 18/05/2015.
 */
public class ReadFileCommand extends Command {

    private String remoteFile;

    public ReadFileCommand(int waitFor, boolean comparable, String remoteFile) {
        super(waitFor, comparable);

        this.remoteFile = remoteFile;
    }

    public ReadFileCommand() {
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

        setCommandResponse(getConnectionManager().readFile(remoteFile));
        return getCommandResponse();
    }
}
