package models;

import java.util.ArrayList;

/**
 * Created by wasinski on 20/04/2015.
 */
public class CommandContainer
{
    private String hostName;
    private ArrayList<Instruction> instructions;

    public CommandContainer(String hostName, ArrayList<Instruction> instructions) {
        this.hostName = hostName;
        this.instructions = instructions;
    }

    public String getHostName() {
        return hostName;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }
}
