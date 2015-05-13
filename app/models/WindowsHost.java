package models;

import com.jcraft.jsch.JSchException;
import command_sets.WindowsCommandSet;
import context.WindowsContext;
import core.CommandResponse;
import core.CommandResponseCode;
import org.joda.time.DateTime;
import predefined_actions.Sg3Utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wasinski on 04/02/2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WindowsHost extends Host {

    /**
     * Creates a new instance of the host object.
     *
     * @param hostName
     * @param username
     * @param password
     */
    public WindowsHost(String hostName, String username, String password, ArrayList<CommandResponse> commands) {
        super(hostName, username, password, commands);
    }

    /**
     * Empty constructor required by JAXB.
     */
    public WindowsHost()
    {
        setContext(new WindowsContext(new WindowsCommandSet()));
    }


    /**
     * Prepares the windows host to ensure it has all the necessary
     * script files uploaded and unzipped before digital id is captured.
     * @throws java.io.IOException
     * @throws JSchException
     */
    @Override
    public void prepare() throws IOException, JSchException
    {


    }

    @Override
    public void retrieveSystemInformation() throws IOException, JSchException
    {
        System.out.println("Collecting system information from host: " + this.getHostName());
        /*getConnectionManager().sendCommand("powershell.exe -executionPolicy bypass -file \"c:\\digital_id\\system_info.ps1", CommandType.Shell);

        CommandResponse<String> commandResponse = getConnectionManager().readFile("c:\\digital_id\\system_info.txt");
        String systemInformation = "";

        if(commandResponse.getCommandResponseCode() == CommandResponseCode.Success)
        {
             systemInformation = commandResponse.getResult();
        }

        setSystemInformation(systemInformation);*/
        //getConnectableInterface().informationRetrieved(getSystemInformation());
    }

    @Override
    public void retrieveMultipathInformation() throws IOException, JSchException
    {
        System.out.println("Collecting mpio information from host: " + this.getHostName());
        /*CommandResponse<String> commandResponse = getConnectionManager().sendCommand("powershell.exe -executionPolicy bypass -file \"c:\\digital_id\\mpio.ps1", CommandType.Shell);

        if(commandResponse.getCommandResponseCode() == CommandResponseCode.Success)
        {
            CommandResponse<String> commandResponse1 = getConnectionManager().readFile("c:\\digital_id\\mpio.txt");

            if(commandResponse1.getCommandResponseCode() == CommandResponseCode.Success)
            {
                String multipathInformation = commandResponse1.getResult();
                setMultipathInformation(multipathInformation);
                //getConnectableInterface().informationRetrieved(getMultipathInformation());
            }
        }*/
    }

    @Override
    public void retrieveSg3UtilsInformation() throws IOException, JSchException
    {
        System.out.println("Collecting sg3utils information from host: " + this.getHostName());

        /*CommandResponse commandResponse = getConnectionManager()
                .sendCommand(new Command("powershell.exe -executionPolicy bypass -file \"c:\\digital_id\\sg3_utils.ps1", 0, true), CommandType.Exec);

        if(commandResponse.getCommandResponseCode() == CommandResponseCode.Success)
        {
            CommandResponse commandResponse1 = getConnectionManager().readFile("c:\\digital_id\\sg3_utils.txt");

            if(commandResponse1.getCommandResponseCode() == CommandResponseCode.Success)
            {
                String sg3utilsInformation = commandResponse1.getResult();
                setSg3utilsInformation(sg3utilsInformation);
                //getConnectableInterface().informationRetrieved(getSg3utilsInformation());
            }
        }*/
    }


    @Override
    public void cleanupHost() throws IOException, JSchException
    {
        System.out.println("Cleaning up host: " + this.getHostName());

    }

    @Override
    public CommandResponse runSg3Utils()
    {
        return Sg3Utils.getWindowsReport(this);
    }
}
