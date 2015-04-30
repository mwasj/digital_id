package context;

import command_sets.WindowsCommandSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WindowsContext extends Context
{
    private static String[] filters = {"MPIO Storage Snapshot.*"};

    public WindowsContext(WindowsCommandSet windowsCommandSet)
    {
        super(filters, windowsCommandSet);
    }
}
