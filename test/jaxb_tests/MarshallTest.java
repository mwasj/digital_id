package jaxb_tests;

import actions.Action;
import commands.Command;
import commands.SendRemoteCommand;
import core.CommandResponse;
import core.CommandResponseCode;
import core.DigitalID;
import core.DigitalIDUtils;
import models.Host;
import models.RemoteCommandType;
import models.WindowsHost;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by wasinski on 21/05/2015.
 */
public class MarshallTest
{
    private DigitalID digitalID;
    private Host windowsHost;
    private ArrayList<Command> commands;
    private ArrayList<Action> actions;

    @Before
    public void setUp()
    {
        commands = new ArrayList<>();
        Command sendRemoteCommand = new SendRemoteCommand("Executing command pwd","pwd", RemoteCommandType.Exec ,0, false);
        sendRemoteCommand.setCommandResponse(new CommandResponse("current dir", CommandResponseCode.Success, ""));
        commands.add(sendRemoteCommand);

        Action action = new Action(commands, "test action");
        actions = new ArrayList<>();
        actions.add(action);

        windowsHost = new WindowsHost("dl380pg8-74", "Administrator", "ssmssm", actions);

        digitalID = new DigitalID("Michal", "testMarshal", null);

        digitalID.addHost(windowsHost);
    }

    @Test
    public void marshallDigitalId() throws InterruptedException {
        //DigitalIdRunner runner = new DigitalIdRunner(digitalID);
        //runner.start();
        //runner.join();

        DigitalIDUtils.marshall(digitalID);
    }
}
