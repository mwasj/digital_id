package models;

import core.CommandResponse;

/**
 * Created by wasinski on 30/04/2015.
 */
public class WebUpdate
{
    private String text;
    private int id;
    private CommandResponse commandResponse;
    private WebUpdateType webUpdateType;

    public WebUpdateType getWebUpdateType() {
        return webUpdateType;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public CommandResponse getCommandResponse() {
        return commandResponse;
    }

    public WebUpdate(String text, int id, CommandResponse commandResponse, WebUpdateType webUpdateType)
    {
        this.text = text;
        this.id = id;
        this.commandResponse = commandResponse;
        this.webUpdateType = webUpdateType;
    }


}
