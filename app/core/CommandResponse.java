package core;

import javax.xml.bind.annotation.*;

/**
 * Represents a command response object.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommandResponse<T>
{
    private T result;

    private CommandResponseCode commandResponseCode;

    private String errorMessage;

    public T getResult() {
        return result;
    }

    public CommandResponseCode getCommandResponseCode() {
        return commandResponseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public CommandResponse(T result, CommandResponseCode commandResponseCode, String errorMessage)
    {
        this.result = result;
        this.commandResponseCode = commandResponseCode;
        this.errorMessage = errorMessage;
    }

    public CommandResponse(){}
}
