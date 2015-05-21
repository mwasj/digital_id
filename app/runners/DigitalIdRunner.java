package runners;

import actions.Action;
import core.DigitalID;
import core.DigitalIDMapper;
import core.DigitalIDUtils;
import core.WebUpdater;
import models.Host;
import models.Inserv;
import models.Switch;

import java.util.ArrayList;

/**
 * Created by wasinski on 15/05/2015.
 */
public class DigitalIdRunner extends Thread
{
    private DigitalID digitalID;
    private String jsonString;
    private String sessionName;
    private WebUpdater webUpdater;
    private DigitalIDMapper digitalIDMapper;

    public DigitalIdRunner(String jsonString, String sessionName)
    {
        this.jsonString = jsonString;
        this.sessionName = sessionName;
        this.digitalIDMapper = new DigitalIDMapper(this.jsonString, this.sessionName);
        this.digitalID = digitalIDMapper.map();
    }

    public DigitalIdRunner(DigitalID digitalID)
    {
        this.digitalID = digitalID;
    }
    public DigitalIdRunner(){}
    public void initialise()
    {
        this.webUpdater = new WebUpdater(digitalID.getSessionName());
    }

    public void sendAnalysis()
    {
        ArrayList<Action> actions = new ArrayList<>();

        for(Host host : digitalID.getHosts())
        {
            for(Action action : host.getActions())
            {
                actions.add(action);
            }
        }

        for(Switch sw : digitalID.getSwitches())
        {
            for(Action action : sw.getActions())
            {
                actions.add(action);
            }
        }

        for(Inserv inserv : digitalID.getInservs())
        {
            for(Action action : inserv.getActions())
            {
                actions.add(action);
            }
        }

        webUpdater.sendAnalysis(actions);
    }

    public void run()
    {
        for(Host host : digitalID.getHosts())
        {
            for(Action action : host.getActions())
            {
                action.initialise(host, sessionName);
                action.perform();
            }

        }

        //TODO - Add the rest of devices.

        DigitalIDUtils.marshall(digitalID);
    }
}
