package digitalId;

import commands.Command;
import commands.SendRemoteCommand;
import core.CommandResponseCode;
import models.RemoteCommandType;
import models.Host;
import models.WindowsHost;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;
import runners.CommandRunner;

import java.util.ArrayList;

/**
 * Created by wasinski on 18/05/2015.
 */
public class CommandRunnerTests
{
    private Host windowsHost;
    private CommandRunner commandRunner;
    private ArrayList<Command> commands;

    @Before
    public void setUp()
    {
        commands = new ArrayList<>();
        commands.add(new SendRemoteCommand("pwd", RemoteCommandType.Exec ,0, 0, true,false));
        windowsHost = new WindowsHost("dl380pg8-74", "Administrator", "ssmssm", commands);
        commandRunner = new CommandRunner(windowsHost);
        commandRunner.initialise();
    }

    @Test
    public void runCommands()
    {
        windowsHost.setCommands(commandRunner.runCommands());

        for(Command command : windowsHost.getCommands())
        {
            Assert.notNull(command.getCommandResponse().getResult(), "Result should not be null.");
            Assert.state(command.getCommandResponse().getCommandResponseCode() == CommandResponseCode.Success, "Make sure the command has succeeded.");
        }

        Assert.state(commands.size() == windowsHost.getCommands().size(), "Both arrayslists should be of the same length.");
    }

}
