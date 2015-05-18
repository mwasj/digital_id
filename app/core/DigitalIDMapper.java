package core;

import commands.Command;
import models.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wasinski on 01/04/2015.
 */
public class DigitalIDMapper
{
    private String jsonString;
    private int webIndex;

    public DigitalIDMapper(String jsonString)
    {
        this.jsonString = jsonString;
        this.webIndex = 1;
    }

    public DigitalID map(String jsonString, String sessionName)
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
                hosts.add(new WindowsHost(hostName, username, password, getCommands(jsonObject.getJSONArray("commands"))));
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
                s = new CiscoSwitch(hostName, username, password, getCommands(jsonObject.getJSONArray("commands")));
            }
            else if(hostType.equals("Brocade"))
            {
                s = new BrocadeSwitch(hostName, username, password, getCommands(jsonObject.getJSONArray("commands")));
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

            inserv = new Inserv(hostName,username,password, getCommands(jsonObject.getJSONArray("commands")));

            arrays.add(inserv);
        }

        return arrays;
    }

    private ArrayList<Command> getCommands(JSONArray jsonArray)
    {
        ArrayList<Command> commands = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

        }

        return commands;
    }



}
