package runners;

import core.DigitalID;
import core.DigitalIDMapper;
import core.WebUpdater;
import models.Host;

/**
 * Created by wasinski on 15/05/2015.
 */
public class DigitalIdRunner extends Thread
{
    private DigitalID digitalID;
    private String jsonString;
    private String sessionName;
    private WebUpdater webUpdater;

    public DigitalIdRunner(String jsonString, String sessionName)
    {
        this.jsonString = jsonString;
        this.sessionName = sessionName;
    }

    public void initialise()
    {
        this.digitalID = DigitalIDMapper.map(jsonString, sessionName);
        this.webUpdater = new WebUpdater(digitalID.getSessionName());
    }

    public void run()
    {
        for(Host host : digitalID.getHosts())
        {
            CommandRunner commandRunner = new CommandRunner(host, webUpdater);
            commandRunner.initialise();
            host.setCommands(commandRunner.runCommands());
        }
    }
}
