package models;

/**
 * Created by Michal on 28/04/2015.
 */
public class Command
{
    private String command;
    private int id;
    private int interval;
    private int iterations;
    private String userText;

    public Command(int id, String command, int interval, int iterations) {
        this.id = id;
        this.command = command;
        this.interval = interval;
        this.iterations = iterations;
    }

    public String getCommand() {
        return command;
    }

    public int getInterval() {
        return interval;
    }

    public int getIterations() {
        return iterations;
    }

    public int getId() {
        return id;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }
}
