package commands;

import core.CommandResponse;
import core.ConnectionManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wasinski on 15/05/2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SendFileCommand extends Command
{
    private String localFile;
    private String remoteFile;

    public SendFileCommand(int id, String displayName,String localFile, String remoteFile)
    {
        super(id, displayName, 0, false);
        this.localFile = localFile;
        this.remoteFile = remoteFile;
    }

    public SendFileCommand()
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

        setCommandResponse(getConnectionManager().sendFile(localFile, remoteFile));
        return getCommandResponse();
    }
}
