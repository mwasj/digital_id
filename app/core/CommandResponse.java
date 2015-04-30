package core;

import models.Command;

/**
 * Created by wasinski on 17/02/2015.
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

    public Command getCommand() {
        return command;
    }

    public CommandResponseCode getCommandResponseCode() {
        return commandResponseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public CommandResponse(T result, CommandResponseCode commandResponseCode, String errorMessage, Command command) {
        this.result = result;
        this.commandResponseCode = commandResponseCode;
        this.errorMessage = errorMessage;
        this.command = command;
    }
}
