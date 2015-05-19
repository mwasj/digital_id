package core;

import actions.Action;
import actions.ActionStatus;
import commands.Command;
import commands.CommandStatus;
import controllers.CaptureUpdater;
import dtos.ActionUpdateDto;
import dtos.AnalysisDto;
import dtos.CommandUpdateDto;
import models.WebUpdate;
import models.WebUpdateType;

import java.util.ArrayList;

/**
 * Created by wasinski on 13/05/2015.
 */
public class WebUpdater
{
    private String sessionName;

    public WebUpdater(String sessionName)
    {
        this.sessionName = sessionName;
    }

    public void sendAnalysis(ArrayList<Action> actions)
    {
        try {
            CaptureUpdater.sendAnalysis(new AnalysisDto(sessionName, actions));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCommandUpdate(String webId, CommandStatus commandStatus, String data)
    {
        CaptureUpdater.sendCommandUpdate(new CommandUpdateDto(webId,commandStatus,data,sessionName));
    }

    public void sendActionUpdate(ActionStatus actionStatus, String webId)
    {
        CaptureUpdater.sendActionUpdate(new ActionUpdateDto(sessionName, webId, actionStatus));
    }
}
