package core;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.*;

/**
 * Represents a command response object.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommandResponse<T>
{
    @Expose
    private T result;
    @Expose
    private CommandResponseCode commandResponseCode;
    @Expose
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
