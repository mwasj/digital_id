package context;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wasinski on 16/02/2015.
 */
@XmlRootElement
public class SwitchPortContext extends  Context
{
    private static String[] filters = {"MPIO Storage Snapshot.*"};
    public SwitchPortContext() {
        super(filters, null);
    }
}
