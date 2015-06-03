package core;

import actions.Action;
import commands.Command;
import commands.SendRemoteCommand;
import models.*;
import org.json.JSONArray;
import org.json.JSONObject;
import predefined_actions.Sg3Utils;

import java.util.ArrayList;

/**
 * Created by wasinski on 01/04/2015.
 */
public class DigitalIDMapper
{
    private String jsonString;
    private String sessionName;
    private int actionIndex;

    public DigitalIDMapper(String jsonString, String sessionName)
    {
        this.jsonString = jsonString;
        this.sessionName = sessionName;
        this.actionIndex = 0;
    }

    public DigitalID map()
    {
        System.out.println("The following json string was received:" + jsonString);

        JSONObject jsnobject = new JSONObject(jsonString);
        String name = jsnobject.getString("digitalIdName");
        String author = jsnobject.getString("digitalIdAuthor");

        DigitalID digitalID = new DigitalID(author, name, sessionName);

        digitalID.addHosts(mapHosts(jsonString));
        digitalID.addSwitches(mapSwitches(jsonString));
        digitalID.addInservs(mapArrays(jsonString));
        return  digitalID;
    }

    private ArrayList<Host> mapHosts(String jsonString)
    {
        ArrayList<Host> hosts = new ArrayList<>();
        JSONObject jsnobject = new JSONObject(jsonString);
        JSONArray jsonArray = jsnobject.getJSONArray("hosts");

        for(int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String hostType = jsonObject.getString("componentType");
            String hostName = jsonObject.getString("hostName");
            String username = jsonObject.getString("userName");
            String password = jsonObject.getString("password");

            if(hostType.equals("Windows"))
            {
                hosts.add(new WindowsHost(hostName, username, password, mapActions(jsonObject.getJSONArray("actions"))));
            }
            else if(hostType.equals("Linux"))
            {

            }
        }

        return hosts;
    }

    private ArrayList<Switch> mapSwitches(String jsonString)
    {
        ArrayList<Switch> switches = new ArrayList<>();

        JSONObject jsnobject = new JSONObject(jsonString);
        JSONArray jsonArray = jsnobject.getJSONArray("switches");

        for(int i = 0; i < jsonArray.length(); i++)
        {
            Switch s = null;
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String hostType = jsonObject.getString("componentType");
            String hostName = jsonObject.getString("hostName");
            String username = jsonObject.getString("userName");
            String password = jsonObject.getString("password");

            if(hostType.equals("Cisco"))
            {
                s = new CiscoSwitch(hostName, username, password, mapActions(jsonObject.getJSONArray("actions")));
            }
            else if(hostType.equals("Brocade"))
            {
                s = new BrocadeSwitch(hostName, username, password, mapActions(jsonObject.getJSONArray("actions")));
            }
            else if(hostType.equals("Qlogic"))
            {

            }

            switches.add(s);
        }

        return switches;
    }

    private ArrayList<Inserv> mapArrays(String jsonString)
    {
        ArrayList<Inserv> arrays = new ArrayList<>();

        JSONObject jsnobject = new JSONObject(jsonString);
        JSONArray jsonArray = jsnobject.getJSONArray("arrays");

        for(int i = 0; i < jsonArray.length(); i++)
        {
            Inserv inserv;
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String hostName = jsonObject.getString("hostName");
            String username = jsonObject.getString("userName");
            String password = jsonObject.getString("password");

            inserv = new Inserv(hostName,username,password, mapActions(jsonObject.getJSONArray("actions")));

            arrays.add(inserv);
        }

        return arrays;
    }

    private ArrayList<Action> mapActions(JSONArray jsonArray)
    {
        ArrayList<Action> actions = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String description = jsonObject.getString("description");
            int actionIndex = jsonObject.getInt("actionIndex");

            //TODO - replace with proper mapper.
            if(actionIndex != 0)
            {
                actions.add(Sg3Utils.windowsSg3UtilsAction());
            }
            else
            {
                JSONArray commandsArray = jsonObject.getJSONArray("commands");
                ArrayList<Command> commands = new ArrayList<>();

                for(int y = 0; y < commandsArray.length(); y++)
                {
                    JSONObject commandJsonObject = commandsArray.getJSONObject(y);

                    String commandString = commandJsonObject.getString("commandString");
                    int interval = commandJsonObject.getInt("interval");
                    boolean comparable = commandJsonObject.getBoolean("comparable");

                    commands.add(new SendRemoteCommand(0,"Executing command: "+commandString, commandString, RemoteCommandType.Exec, interval,comparable));

                }

                actions.add(new Action(actionIndex, commands, description));
                actionIndex += 1;
            }
        }

        return actions;
    }



}
