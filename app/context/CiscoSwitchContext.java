package context;

import command_sets.CiscoCommandSet;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wasinski on 16/02/2015.
 */
@XmlRootElement
public class CiscoSwitchContext extends Context
{
    private static String[] filters = {"Kernel uptime is.*"};



    public CiscoSwitchContext(CiscoCommandSet ciscoCommandSet)
    {
        super(filters, ciscoCommandSet);
    }


}
