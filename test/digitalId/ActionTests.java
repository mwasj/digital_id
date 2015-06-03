package digitalId;

import actions.Action;
import actions.ActionType;
import commands.Command;
import commands.SendRemoteCommand;
import core.CommandResponseCode;
import models.RemoteCommandType;
import models.Host;
import models.WindowsHost;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;
import predefined_actions.Sg3Utils;
import runners.CommandRunner;

import java.util.ArrayList;

/**
 * Created by wasinski on 18/05/2015.
 */
public class ActionTests
{
    private Host windowsHost;
    private ArrayList<Command> commands;
    private ArrayList<Action> actions;
    @Before
    public void setUp()
    {
        commands = new ArrayList<>();
        commands.add(new SendRemoteCommand(0,"Executing command pwd","pwd", RemoteCommandType.Exec ,0, false));

        Action action = new Action(0, commands, "test action");
        actions = new ArrayList<>();
        actions.add(action);
        
        windowsHost = new WindowsHost("dl380pg8-74", "Administrator", "ssmssm", actions);
    }

    @Test
    public void performActions()
    {
        for(Action action : windowsHost.getActions())
        {
            action.initialise(windowsHost, null);
            action.perform();

            for(Command command : action.getCommands())
            {
                Assert.notNull(command.getCommandResponse().getResult(), "Result should not be null.");
                Assert.state(command.getCommandResponse().getCommandResponseCode() == CommandResponseCode.Success, "Make sure the command has succeeded.");
            }
        }

        Assert.state(commands.size() == windowsHost.getActions().size(), "Both arrayslists should be of the same length.");
    }

    @Test
    public void performSg3UtilsAction()
    {
        Action a = Sg3Utils.windowsSg3UtilsAction();
        a.initialise(windowsHost, null);

        actions.add(a);

        for(Action action : windowsHost.getActions())
        {
            action.initialise(windowsHost, null);
            action.perform();

            for(Command command : action.getCommands())
            {
                Assert.state(command.getCommandResponse().getCommandResponseCode() == CommandResponseCode.Success, "Make sure the command has succeeded.");

                if(command.getCommandResponse().getResult() != null)
                {
                    System.out.println(command.getCommandResponse().getResult());
                }
            }
        }
    }
}
