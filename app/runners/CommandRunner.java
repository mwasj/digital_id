package runners;

import commands.Command;
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

    public CommandRunner(ConnectionManager connectionManager, ArrayList<Command> commands)
    {
        this.commands = commands;
        this.connectionManager = connectionManager;
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
            c.execute();

            if(c.getWaitForValue() > 0)
            {
                long id =  Calendar.getInstance().getTimeInMillis();
                int seconds = (c.getWaitForValue());

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
