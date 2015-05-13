package models;

import com.jcraft.jsch.JSchException;
import context.Context;
import core.CommandResponse;
import core.DigitalID;
import interfaces.HostPredefinedActionListener;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wasinski on 27/01/2015.
 */
@XmlSeeAlso({WindowsHost.class})
public abstract class Host extends Connectable implements HostPredefinedActionListener
{
    private String systemInformation;
    private String multipathInformation;
    private String sg3utilsInformation;

    public String getSystemInformation() {
        return systemInformation;
    }

    public String getMultipathInformation() {
        return multipathInformation;
    }

    public String getSg3utilsInformation() {
        return sg3utilsInformation;
    }

    public void setSystemInformation(String systemInformation) {
        this.systemInformation = systemInformation;
    }

    public void setSg3utilsInformation(String sg3utilsInformation) {
        this.sg3utilsInformation = sg3utilsInformation;
    }

    public void setMultipathInformation(String multipathInformation) {
        this.multipathInformation = multipathInformation;
    }

    protected Host(){}

    /**
     * Creates a new instance of the host object.
     * @param hostName
     * @param username
     * @param password
     */
    protected Host(String hostName, String username, String password, ArrayList<CommandResponse> commands)
    {
        super(hostName, username, password, commands);
    }

    public abstract void prepare() throws IOException, JSchException;

    public abstract void retrieveSystemInformation() throws IOException, JSchException;

    public abstract void retrieveMultipathInformation() throws IOException, JSchException;

    public abstract void retrieveSg3UtilsInformation() throws IOException, JSchException;

    public abstract void cleanupHost() throws IOException, JSchException;
}
