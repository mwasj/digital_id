package predefined_actions;

import actions.Action;
import com.jcraft.jsch.JSchException;
import commands.Command;
import commands.ReadFileCommand;
import commands.SendFileCommand;
import commands.SendRemoteCommand;
import core.CommandResponse;
import models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wasinski on 13/05/2015.
 */
public class Sg3Utils
{
    public static Action windowsSg3UtilsAction()
    {
        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new SendFileCommand("Sending file C:/digital_id.zip","C:/digital_id.zip", "c:\\digital_id.zip"));
        commands.add(new SendRemoteCommand("Extracting archive","powershell.exe -noprofile -command \"&{ $shell = new-object -com shell.application; $zip = $shell.NameSpace(\"'C:\\digital_id.zip'\"); foreach($item in $zip.items()) { $shell.Namespace(\"'C:\\'\").copyhere($item, 0x10); }}\"", RemoteCommandType.Shell, 0, false));
        commands.add(new SendRemoteCommand("Running scripts","powershell.exe -executionPolicy bypass -file \"c:\\digital_id\\sg3_utils.ps1", RemoteCommandType.Shell, 0, false));
        commands.add(new ReadFileCommand("Capturing results","c:\\digital_id\\sg3_utils.txt", 0, true));
        commands.add(new SendRemoteCommand("Performing initial cleanup","powershell.exe  -noprofile -command \"&{ Remove-item c:\\digital_id -Force -Recurse }\"", RemoteCommandType.Shell, 0,false));
        commands.add(new SendRemoteCommand("Performing secondary cleanup","powershell.exe  -noprofile -command \"&{ Remove-item c:\\digital_id.zip -Force }\"", RemoteCommandType.Shell, 0,false));

        Action sg3UtilsAction = new Action(commands, "Run Sg3utils");

        return sg3UtilsAction;
    }
}
