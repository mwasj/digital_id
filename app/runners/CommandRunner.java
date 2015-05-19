package runners;

import actions.ActionStatus;
import commands.Command;
import commands.CommandStatus;
import commands.CommandUpdateInterface;
import core.CommandResponseCode;
import core.ConnectionManager;
import core.WebUpdater;
import models.*;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wasinski on 12/05/2015.
 */
public class CommandRunner
{
    private ConnectionManager connectionManager;
    private ArrayList<Command> commands;
    private CommandUpdateInterface commandUpdateInterface;

    public CommandRunner(ConnectionManager connectionManager, ArrayList<Command> commands, CommandUpdateInterface commandUpdateInterface)
    {
        this.commands = commands;
        this.connectionManager = connectionManager;
        this.commandUpdateInterface = commandUpdateInterface;
    }

    public void initialise()
    {
        connectionManager.connect();
    }

    public ArrayList<Command> runCommands()
    {
        for(Command c : commands)
        {
            c.initialise(connectionManager);

            c.setCommandStatus(CommandStatus.Executing);
            commandUpdateInterface.sendCommandUpdate(c.getCommandStatus(), null, c.getWebId());
            c.execute();
            c.setCommandStatus(c.getCommandResponse().getCommandResponseCode() == CommandResponseCode.Success ? CommandStatus.Pass : CommandStatus.Fail);
            commandUpdateInterface.sendCommandUpdate(c.getCommandStatus(), (String) c.getCommandResponse().getResult(), c.getWebId());

            if(c.getWaitForValue() > 0)
            {
                //connectable.getWebUpdater().sendUpdate(new WebUpdate("Sleeping for: " + seconds + " seconds", id, null, WebUpdateType.progressUpdate));

                try {
                    Thread.sleep(c.getWaitForValue() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                    /*connectable.getWebUpdater().sendUpdate(new WebUpdate("Resuming after " + seconds + " seconds", id,
                            new CommandResponse("Resuming after " + seconds + " seconds", CommandResponseCode.Success, null, null, null, null), WebUpdateType.progressUpdate));*/
            }
        }

        return commands;
    }
}
