package core;

import com.jcraft.jsch.JSchException;
import command_sets.SwitchCommandSet;
import models.CommandType;
import models.Switch;

import java.io.IOException;

/**
 * Created by wasinski on 19/02/2015.
 */
public class SwitchHelper
{
    public static String getSwitchInformation(Switch s) throws IOException, JSchException
    {

        return null;
    }

    public static String getFlogiDatabase(Switch s) throws IOException, JSchException
    {

        return null;
    }

    public static String getPortInformatiuon(Switch s, String portNumber) throws IOException, JSchException
    {
        System.out.println("Capturing information for port: " + portNumber);

        return null;
    }
}
