package context;

import command_sets.SwitchCommandSet;

/**
 * Created by wasinski on 19/02/2015.
 */
public class SwitchContext extends Context
{
    private SwitchCommandSet switchCommandSet;

    public SwitchContext(String[] filters, SwitchCommandSet switchCommandSet) {
        super(filters, switchCommandSet);

        this.switchCommandSet = switchCommandSet;
    }

    public SwitchCommandSet getSwitchCommandSet()
    {
        return switchCommandSet;
    }
}
