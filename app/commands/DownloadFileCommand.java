package commands;

import core.CommandResponse;
import core.ConnectionManager;

/**
 * Created by Michal on 18/05/2015.
 */
public class DownloadFileCommand extends Command {

    private String remoteFile;
    private String localFile;

    public DownloadFileCommand(String displayName, int waitFor, String remoteFile, String localFile) {
        super(displayName, waitFor, false);

        this.remoteFile = remoteFile;
        this.localFile = localFile;
    }

    public DownloadFileCommand() {
    }

    @Override
    public void initialise(ConnectionManager connectionManager) {
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

        setCommandResponse(getConnectionManager().getFile(localFile, remoteFile));
        return getCommandResponse();
    }
}
