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

    public Command(String command, int interval, boolean comparable)
    {
        this.command = command;
        this.interval = interval;
        this.comparable = comparable;
    }

    public Command(){}
    public String getCommand() {
        return command;
    }

    public int getInterval() {
        return interval;
    }
}
