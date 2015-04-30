package context;

import command_sets.CommandSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Created by wasinski on 16/02/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({WindowsContext.class, CiscoSwitchContext.class, InservContext.class, SwitchPortContext.class})
public abstract class Context
{
    private String[] filters;

    private CommandSet commandSet;

    public Context(String[] filters, CommandSet commandSet)
    {
        this.filters = filters;
        this.commandSet = commandSet;
    }

    public String applyDiffFilters(String s) {
        for (String s1 : filters) {
            s = s.replaceAll(s1, "");
        }

        return s;
    }

    public CommandSet getCommandSet() {
        return commandSet;
    }
}

