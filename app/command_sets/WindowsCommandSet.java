package command_sets;

/**
 * Created by wasinski on 19/02/2015.
 */
public class WindowsCommandSet extends  HostCommandSet
{
    public String EXTRACT_ZIP_FILE() { return "powershell.exe -noprofile -command \"&{ $shell = new-object -com shell.application; $zip = $shell.NameSpace(\"'C:\\digital_id.zip'\"); foreach($item in $zip.items()) { $shell.Namespace(\"'C:\\'\").copyhere($item, 0x10); }}\""; }
}
