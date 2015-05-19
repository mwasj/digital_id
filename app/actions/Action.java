package actions;

import com.google.gson.annotations.Expose;
import commands.Command;
import commands.CommandStatus;
import commands.CommandUpdateInterface;
import core.CommandResponseCode;
import core.ConnectionManager;
import core.WebUpdater;
import dtos.CommandUpdateDto;
import models.Connectable;
import runners.CommandRunner;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Michal on 18/05/2015.
 */
public class Action implements CommandUpdateInterface
{
    @Expose
    private ArrayList<Command> commands;
    @Expose
    private String name;

    private ActionType actionType;
    @Expose
    private String webId;
    @Expose
    private ActionStatus actionStatus;

    public ActionStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(ActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }

    private ConnectionManager connectionManager;
    private WebUpdater webUpdater;
    private CommandRunner commandRunner;
    public ArrayList<Command> getCommands()
    {
        return commands;
    }

    public ActionType getActionType()
    {
        return actionType;
    }

    /**
     * Initialises a new instance of the Action object.
     * @param commands
     * @param name
     */
    public Action(ArrayList<Command> commands, String name)
    {
        this.commands = commands;
        this.name = name;
        this.webId = UUID.randomUUID().toString().replaceAll("-","");
        this.actionStatus = ActionStatus.NotRun;
    }

    public Action()
    {
        this.webId = UUID.randomUUID().toString().replaceAll("-", "");
        this.actionStatus = ActionStatus.NotRun;
    }

    public void perform()
    {
        setActionStatus(ActionStatus.Executing);
        webUpdater.sendActionUpdate(getActionStatus(), webId);

        if(commandRunner == null)
        {
            System.out.println("Action was not initialised. It will not be performed.");
            return;
        }

        commands = commandRunner.runCommands();

        setActionStatus(determineStatus());

        webUpdater.sendActionUpdate(getActionStatus(), webId);
    }

    private ActionStatus determineStatus()
    {
        ActionStatus status = ActionStatus.Pass;

        for(Command command : getCommands())
        {
            if(command.getCommandResponse().getCommandResponseCode() == CommandResponseCode.Failure)
            {
                status = ActionStatus.Fail;
            }
        }

        return status;
    }

    public void initialise(Connectable connectable, String sessionName)
    {
        webUpdater = new WebUpdater(sessionName);

        connectionManager = new ConnectionManager(connectable);

        commandRunner = new CommandRunner(connectionManager, commands, this);
        commandRunner.initialise();
    }


    @Override
    public void sendCommandUpdate(CommandStatus commandStatus, String data, String webId)
    {
        webUpdater.sendCommandUpdate(webId, commandStatus, data);
    }
}
