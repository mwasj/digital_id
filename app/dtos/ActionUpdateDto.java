package dtos;

import actions.ActionStatus;

/**
 * Created by wasinski on 19/05/2015.
 */
public class ActionUpdateDto
{
    private String webId;
    private ActionStatus actionStatus;
    private String userName;

    public String getWebId() {
        return webId;
    }

    public ActionStatus getActionStatus() {
        return actionStatus;
    }

    public String getUserName() {
        return userName;
    }

    public ActionUpdateDto(String userName,String webId, ActionStatus actionStatus) {
        this.userName = userName;
        this.webId = webId;
        this.actionStatus = actionStatus;
    }
}
