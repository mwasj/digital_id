package actions;

import commands.Command;
import core.ConnectionManager;
import core.WebUpdater;
import models.Connectable;
import runners.CommandRunner;

import java.util.ArrayList;

/**
 * Created by Michal on 18/05/2015.
 */
public class Action
{
    private ArrayList<Command> commands;
    private String name;
    private String webId;
    private ConnectionManager connectionManager;
    private Connectable connectable;
    private WebUpdater webUpdater;
    private CommandRunner commandRunner;

    public Action(ArrayList<Command> commands, String name) {
        this.commands = commands;
        this.name = name;
    }

    public void perform()
    {
        if(commandRunner == null)
        {
            System.out.println("Action was not initialised. It will not be performed.");
            return;
        }

        commands = commandRunner.runCommands();
    }

    public void initialise(Connectable connectable, String sessionName)
    {
        webUpdater = new WebUpdater(sessionName);

        this.connectable = connectable;
        connectionManager = new ConnectionManager(connectable);

        commandRunner = new CommandRunner(connectionManager, commands);
        commandRunner.initialise();
    }
}
