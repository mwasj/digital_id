package core;

import com.jcraft.jsch.JSchException;
import models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wasinski on 12/05/2015.
 */
public class CommandRunner
{
    private Connectable connectable;
    private ConnectionManager connectionManager;

    public CommandRunner(Connectable connectable)
    {
        this.connectable = connectable;
        this.connectionManager = new ConnectionManager(connectable);
    }

    public void initialise()
    {
        connectionManager.connect();
    }

    public void analyse()
    {

    }

    public ArrayList<CommandResponse> execute()
    {
        ArrayList<CommandResponse> responses = new ArrayList<>();

        for(CommandResponse commandResponse : connectable.getCommandResponses())
        {
            try
            {
                if(commandResponse.getCommand().getId() == 0)
                {
                    responses.add(connectable.getConnectionManager().sendCommand(commandResponse.getCommand(), CommandType.Exec));

                    if(commandResponse.getCommand().getInterval() > 0)
                    {
                        long id =  Calendar.getInstance().getTimeInMillis();
                        int seconds = (commandResponse.getCommand().getInterval());

                        connectable.getWebUpdater().sendUpdate(new WebUpdate("Sleeping for: " + seconds + " seconds", id, null, WebUpdateType.progressUpdate));

                        Thread.sleep(commandResponse.getCommand().getInterval() * 1000);

                        connectable.getWebUpdater().sendUpdate(new WebUpdate("Resuming after " + seconds + " seconds", id,
                                new CommandResponse("Resuming after " + seconds + " seconds", CommandResponseCode.Success, null, null, null, null), WebUpdateType.progressUpdate));
                    }
                }
                else
                {
                    switch(commandResponse.getCommand().getId())
                    {
                        case 1:
                            if(connectable instanceof Host)
                            {
                                responses.add(((Host) connectable).runSg3Utils());
                            }

                    }
                }


            } catch (IOException | JSchException | InterruptedException e)
            {
                e.printStackTrace();
            }

        }

        return responses;
    }
}
