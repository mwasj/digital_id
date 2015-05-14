package models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the command object being executed on the device.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Command
{
    private String command;

    private int interval;

    private boolean comparable;

    public boolean isComparable() {
        return comparable;
    }

    private int id;

    public int getId() {
        return id;
    }

    private boolean causesWebUpdate;

    public boolean isCausingWebUpdate() {
        return causesWebUpdate;
    }

    public Command(String command, int interval, boolean comparable, int id, boolean causesWebUpdate)
    {
        this.command = command;
        this.interval = interval;
        this.comparable = comparable;
        this.id = id;
        this.causesWebUpdate = causesWebUpdate;
    }

    public void execute()
    {

    }

    public Command(){}
    public String getCommand() {
        return command;
    }

    public int getInterval() {
        return interval;
    }
}
