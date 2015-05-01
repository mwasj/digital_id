package core;

import models.Command;
import org.joda.time.DateTime;

/**
 * Represents a command response object.
 */
public class CommandResponse<T>
{
    private T result;
    private CommandResponseCode commandResponseCode;
    private String errorMessage;
    private Command command;
    public T getResult() {
        return result;
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

    public Command getCommand() {
        return command;
    }

    public CommandResponseCode getCommandResponseCode() {
        return commandResponseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public CommandResponse(T result, CommandResponseCode commandResponseCode, String errorMessage, Command command, DateTime executionStartTime, DateTime executionFinishTime)
    {
        this.result = result;
        this.commandResponseCode = commandResponseCode;
        this.errorMessage = errorMessage;
        this.command = command;
        this.executionFinishTime = executionFinishTime;
        this.executionStartTime = executionStartTime;
    }
}
