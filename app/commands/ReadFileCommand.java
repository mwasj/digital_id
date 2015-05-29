package commands;

import core.CommandResponse;
import core.ConnectionManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Michal on 18/05/2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadFileCommand extends Command {

    private String remoteFile;

    public ReadFileCommand(int id, String displayName, String remoteFile, int waitFor, boolean comparable) {
        super(id, displayName, waitFor, comparable);

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
