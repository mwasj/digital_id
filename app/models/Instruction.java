package models;

/**
 * Created by wasinski on 20/04/2015.
 */
public class Instruction
{
    private String command;
    private int index;

    public Instruction(String command, int index) {
        this.command = command;
        this.index = index;
    }

    public String getCommand() {
        return command;
    }

    public int getIndex() {
        return index;
    }
}
