package dtos;

import actions.Action;

import java.util.ArrayList;

/**
 * Created by wasinski on 19/05/2015.
 */
public class AnalysisDto
{
    private ArrayList<Action> actions;
    private String userName;

    public ArrayList<Action> getActions() {
        return actions;
    }

    public String getUserName() {
        return userName;
    }

    public AnalysisDto(String userName ,ArrayList<Action> actions) {
        this.actions = actions;
        this.userName = userName;
    }
}
