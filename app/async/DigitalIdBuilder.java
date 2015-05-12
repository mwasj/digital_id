package async;

import com.jcraft.jsch.JSchException;
import core.*;
import models.*;

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

    /**
     * Initialises a new instance of the DigitalIdBuilder object.
     * @param jsonString
     * @param sessionName
     */
    public DigitalIdBuilder(String jsonString, String sessionName)
    {
        this.jsonString = jsonString;
        this.sessionName = sessionName;
        digitalID = DigitalIDMapper.map(jsonString, sessionName);
    }

    /**
     * Starts the thread which performs the digital id build process.
     */
    public void run()
    {
        try
        {
            digitalID.buildDigitalID();
        }
        catch (IOException | JSchException e)
        {
            long id = DigitalIDUtils.getUniqueID();
            digitalID.update(new WebUpdate("An exception has occurred, ", id, null, WebUpdateType.progressUpdate));
            digitalID.update(new WebUpdate(null, id,
                    new CommandResponse(e.getMessage(), CommandResponseCode.Failure,
                            "An exception has occurred, ", new Command("An exception has occurred, ", 0, false), null, null), WebUpdateType.progressUpdate));
        }
    }
}
