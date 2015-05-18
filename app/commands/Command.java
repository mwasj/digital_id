package commands;

import core.CommandResponse;
import core.ConnectionManager;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the command object being executed on the device.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Command implements CommandInterface
{
    private int waitFor;
    private boolean comparable;
    private CommandType commandType;
    private boolean causesWebUpdate;
    private CommandResponse commandResponse;
    private ConnectionManager connectionManager;
    private int webId;

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

    private DateTime executionStartTime;

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

    public CommandType getCommandType() {
        return commandType;
    }

    public boolean causesWebUpdate()
    {
        return causesWebUpdate;
    }

    public int getWaitForValue()
    {
        return waitFor;
    }

    public int getWebId() {
        return webId;
    }

    public void setWebId(int webId) {
        this.webId = webId;
    }

    public Command(CommandType commandType, int waitFor, boolean comparable, boolean causesWebUpdate)
    {
        this.waitFor = waitFor;
        this.comparable = comparable;
        this.commandType = commandType;
        this.causesWebUpdate = causesWebUpdate;
    }

    /**
     * Empty constructor required by JAXB.
     */
    public Command(){}
}
