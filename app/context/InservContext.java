package context;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wasinski on 16/02/2015.
 */
@XmlRootElement
public class InservContext extends Context
{
    private static String[] filters = {"MPIO Storage Snapshot.*"};

    public InservContext() {
        super(filters, null);
    }
}
