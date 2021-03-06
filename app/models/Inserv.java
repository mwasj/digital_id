package models;

import com.jcraft.jsch.JSchException;
import context.InservContext;
import core.CommandResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Michal on 13/12/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Inserv extends Connectable
{
    public Inserv()
    {
        this.hostDetailedInformation = "";
        this.hostLesbInformation = "";
        this.hostPathSumInformation = "";
        setContext(new InservContext());
    }

    @Override
    public void runCommands()
    {
        ArrayList<CommandResponse> responses = new ArrayList<>();

        for(CommandResponse commandResponse : getCommandResponses())
        {
            try {
                responses.add(getConnectionManager().sendCommand(commandResponse.getCommand(), CommandType.Exec));
                Thread.sleep(commandResponse.getCommand().getInterval()*1000);
            } catch (IOException | JSchException | InterruptedException e) {
                e.printStackTrace();
            }

        }

        setCommandResponses(responses);
    }

    private String inservName;

    public String getHostDetailedInformation() {
        return hostDetailedInformation;
    }

    private String hostDetailedInformation;
    private String hostLesbInformation;
    private String hostPathSumInformation;

    public String getHostLesbInformation() {
        return hostLesbInformation;
    }

    public String getHostPathSumInformation() {
        return hostPathSumInformation;
    }

    public Inserv(String hostName, String userName, String password, ArrayList<CommandResponse> commands) {
        super(hostName, userName, password, commands);
        this.hostDetailedInformation = "";
        this.hostLesbInformation = "";
        this.hostPathSumInformation = "";
    }

    public String getInservName() {
        return inservName;
    }

    public void retrieveHostDetailedInfo(String hostName) throws IOException, JSchException
    {
        System.out.println("Collecting information from inserv: " + this.getHostName());

        //getConnectableInterface().informationRetrieved(getHostDetailedInformation());
    }

    public void retrieveHostLesbInfo(String hostName) throws IOException, JSchException
    {

        //getConnectableInterface().informationRetrieved(getHostLesbInformation());
    }

    public void retrieveHostPathSumInfo(String hostName) throws IOException, JSchException
    {


        //getConnectableInterface().informationRetrieved(getHostPathSumInformation());
    }
}
