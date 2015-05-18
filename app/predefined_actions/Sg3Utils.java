package predefined_actions;

import com.jcraft.jsch.JSchException;
import commands.Command;
import core.CommandResponse;
import models.*;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by wasinski on 13/05/2015.
 */
public class Sg3Utils
{
    public static CommandResponse getWindowsReport(Connectable connectable)
    {
        /*long id =  Calendar.getInstance().getTimeInMillis();
        connectable.getWebUpdater().sendUpdate(new WebUpdate("sg3utils", id, null, WebUpdateType.progressUpdate));

        //Send the zip file to the host.
        connectable.getConnectionManager().sendFile("C:/digital_id.zip", "c:\\digital_id.zip");

        CommandResponse sg3UtilsResponse = null;

        try {
            //Send command to extract the file.
            connectable.getConnectionManager().sendCommand(new Command("powershell.exe -noprofile -command \"&{ $shell = new-object -com shell.application; $zip = $shell.NameSpace(\"'C:\\digital_id.zip'\"); foreach($item in $zip.items()) { $shell.Namespace(\"'C:\\'\").copyhere($item, 0x10); }}\"", 0, true, 0, false), CommandType.Shell);

            //Run the PowerShell script to generate the report.
            connectable.getConnectionManager()
                    .sendCommand(new Command("powershell.exe -executionPolicy bypass -file \"c:\\digital_id\\sg3_utils.ps1", 0, true, 0, false), CommandType.Shell);

            //Retrieve the contents of the report.
            sg3UtilsResponse = connectable.getConnectionManager().readFile("c:\\digital_id\\sg3_utils.txt");

            //Perform a cleanup on the host.
            connectable.getConnectionManager().sendCommand(new Command("powershell.exe  -noprofile -command \"&{ Remove-item c:\\digital_id -Force -Recurse }\"",0,false,0,false), CommandType.Shell);
            connectable.getConnectionManager().sendCommand(new Command("powershell.exe  -noprofile -command \"&{ Remove-item c:\\digital_id.zip -Force }\"",0,false,0,false), CommandType.Shell);
        }
            catch (IOException | JSchException e)
        {
            e.printStackTrace();
        }

        connectable.getWebUpdater().sendUpdate(new WebUpdate("sg3utils", id, sg3UtilsResponse, WebUpdateType.progressUpdate));

        return new CommandResponse(sg3UtilsResponse.getResult(), sg3UtilsResponse.getCommandResponseCode(),null, new Command("sg3utils",0,true,0,true),null,null);*/

        return null;
    }
}
