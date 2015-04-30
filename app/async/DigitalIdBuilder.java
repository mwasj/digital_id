package async;

import com.jcraft.jsch.JSchException;
import core.DigitalID;
import core.DigitalIDMapper;
import models.CommandContainer;
import models.Instruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wasinski on 15/04/2015.
 */
public class DigitalIdBuilder extends Thread
{
    private String jsonString;
    private String sessionName;
    private DigitalID digitalID;
    private ArrayList<CommandContainer> analysisResults;

    public ArrayList<CommandContainer> getAnalysisResults()
    {
        return analysisResults;
    }

    public DigitalIdBuilder(String jsonString, String sessionName)
    {
        this.jsonString = jsonString;
        this.sessionName = sessionName;
        digitalID = DigitalIDMapper.map(jsonString, sessionName);
        analysisResults = digitalID.analyse();
    }

    public void run()
    {
        digitalID.initialise();

        try {
            digitalID.buildDigitalID();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}
