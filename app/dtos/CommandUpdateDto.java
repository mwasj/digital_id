package dtos;

import actions.ActionStatus;
import commands.CommandStatus;

/**
 * Created by wasinski on 19/05/2015.
 */
public class CommandUpdateDto
{
    private String webId;
    private CommandStatus commandStatus;
    private String userName;
    private String data;

    public String getWebId() {
        return webId;
    }

    public CommandStatus getCommandStatus() {
        return commandStatus;
    }

    public String getUserName() {
        return userName;
    }

    public String getData() {
        return data;
    }

    public CommandUpdateDto(String webId, CommandStatus commandStatus, String data, String userName) {
        this.webId = webId;
        this.commandStatus = commandStatus;
        this.data = data;
        this.userName = userName;
    }
}
