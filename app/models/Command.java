package models;

/**
 * Represents the command object being executed on the device.
 */
public class Command
{
    private String command;
    private int interval;

    public Command(String command, int interval)
    {
        this.command = command;
        this.interval = interval;
    }

    public String getCommand() {
        return command;
    }

    public int getInterval() {
        return interval;
    }
}
