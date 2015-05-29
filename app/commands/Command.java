package commands;

import com.google.gson.annotations.Expose;
import core.CommandResponse;
import core.ConnectionManager;
import models.WindowsHost;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.*;
import java.util.UUID;

/**
 * Represents the command object being executed on the device.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({DownloadFileCommand.class, SendFileCommand.class, SendRemoteCommand.class, ReadFileCommand.class})
public abstract class Command implements CommandInterface
{
    @Expose
    private int waitFor;
    @Expose
    private boolean comparable;
    @Expose
    private CommandResponse commandResponse;
    @XmlTransient
    private ConnectionManager connectionManager;

    @Expose
    private String webId;

    @Expose
    private CommandStatus commandStatus;

    private int id;
    @Expose
    private String displayName;

    public CommandStatus getCommandStatus() {
        return commandStatus;
    }

    public void setCommandStatus(CommandStatus commandStatus) {
        this.commandStatus = commandStatus;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public CommandResponse getCommandResponse()
    {
        return commandResponse;
    }

    public void setCommandResponse(CommandResponse commandResponse1)
    {
        this.commandResponse = commandResponse1;
    }
    @Expose
    private DateTime executionStartTime;
    @Expose
    private DateTime executionFinishTime;

    public DateTime getExecutionStartTime() {
        return executionStartTime;
    }

    public DateTime getExecutionFinishTime() {
        return executionFinishTime;
    }

    public void setExecutionStartTime(DateTime executionStartTime) {
        this.executionStartTime = executionStartTime;
    }

    public void setExecutionFinishTime(DateTime executionFinishTime) {
        this.executionFinishTime = executionFinishTime;
    }

    public boolean isComparable()
    {
        return comparable;
    }

    public int getWaitForValue()
    {
        return waitFor;
    }

    public String getWebId() {
        return webId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    public Command(int id, String displayName, int waitFor, boolean comparable)
    {
        this.id = id;
        this.displayName = displayName;
        this.waitFor = waitFor;
        this.comparable = comparable;
        this.webId = UUID.randomUUID().toString().replaceAll("-","");
        this.commandStatus = CommandStatus.NotRun;
    }

    /**
     * Empty constructor required by JAXB.
     */
    public Command()
    {
        this.webId = UUID.randomUUID().toString().replaceAll("-","");
    }
}
