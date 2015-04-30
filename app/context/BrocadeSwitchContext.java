package context;

import command_sets.CommandSet;

/**
 * Created by wasinski on 19/02/2015.
 */
public class BrocadeSwitchContext extends Context
{
    private static String[] filters = {"MPIO Storage Snapshot.*"};

    public BrocadeSwitchContext(CommandSet commandSet) {
        super(filters, commandSet);
    }
}
