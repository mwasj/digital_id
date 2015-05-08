package core;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import models.*;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by wasinski on 01/04/2015.
 */
public class DigitalIDMapper
{
    public static DigitalID map(String jsonString, String sessionName)
    {
        System.out.println(DateTime.now() + "DigitalID mapper: The following JSON String was received: " + jsonString);
        JSONObject jsnobject = new JSONObject(jsonString);
        String name = jsnobject.getString("digitalIdName");
        String author = jsnobject.getString("digitalIdAuthor");
        System.out.println(author);
        DigitalID digitalID = new DigitalID(author, name, sessionName);
        digitalID.addHosts(mapHosts(jsonString));
        digitalID.addSwitches(mapSwitches(jsonString));
        digitalID.addInservs(mapArrays(jsonString));
        return  digitalID;
    }

    private static ArrayList<Host> mapHosts(String jsonString)
    {
        ArrayList<Host> hosts = new ArrayList<>();

        JSONObject jsnobject = new JSONObject(jsonString);
        JSONArray jsonArray = jsnobject.getJSONArray("hosts");

        for(int i = 0; i < jsonArray.length(); i++)
        {
            Host host = null;
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String hostType = jsonObject.getString("componentType");
            String hostName = jsonObject.getString("hostName");
            String username = jsonObject.getString("userName");
            String password = jsonObject.getString("password");
            Type type = new TypeToken<ArrayList<Command>>(){}.getType();
            ArrayList<Command> commands = new Gson().fromJson(jsonObject.getJSONArray("commands").toString(), type);
            ArrayList<CommandResponse> commandResponses = new ArrayList<>();
            for(Command c : commands)
            {
                commandResponses.add(new CommandResponse(null, null, null, c, null, null));
            }
            if(hostType.equals("Windows"))
            {
                host = new WindowsHost(hostName, username, password, commandResponses);
            }
            else if(hostType.equals("Linux"))
            {

            }
            hosts.add(host);
        }

        return hosts;
    }

    private static ArrayList<Switch> mapSwitches(String jsonString)
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
            Type type = new TypeToken<ArrayList<Command>>(){}.getType();
            ArrayList<Command> commands = new Gson().fromJson(jsonObject.getJSONArray("commands").toString(), type);
            ArrayList<CommandResponse> commandResponses = new ArrayList<>();
            for(Command c : commands)
            {
                commandResponses.add(new CommandResponse(null, null, null, c, null, null));
            }
            if(hostType.equals("Cisco"))
            {
                s = new CiscoSwitch(hostName, username, password, null, commandResponses);
            }
            else if(hostType.equals("Brocade"))
            {
                s = new BrocadeSwitch(hostName, username, password, null, commandResponses);
            }
            else if(hostType.equals("Qlogic"))
            {

            }

            switches.add(s);
        }

        return switches;
    }

    private static ArrayList<Inserv> mapArrays(String jsonString)
    {
        ArrayList<Inserv> arrays = new ArrayList<>();

        JSONObject jsnobject = new JSONObject(jsonString);
        JSONArray jsonArray = jsnobject.getJSONArray("arrays");

        for(int i = 0; i < jsonArray.length(); i++)
        {
            Inserv inserv = null;
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String hostName = jsonObject.getString("hostName");
            String username = jsonObject.getString("userName");
            String password = jsonObject.getString("password");
            Type type = new TypeToken<ArrayList<Command>>(){}.getType();
            ArrayList<Command> commands = new Gson().fromJson(jsonObject.getJSONArray("commands").toString(), type);
            ArrayList<CommandResponse> commandResponses = new ArrayList<>();
            for(Command c : commands)
            {
                commandResponses.add(new CommandResponse(null, null, null, c, null, null));
            }
            inserv = new Inserv(hostName,username,password, commandResponses);

            arrays.add(inserv);
        }

        return arrays;
    }
}
