package core;

import com.jcraft.jsch.JSchException;
import controllers.DigitalIdController;
import interfaces.WebUpdater;
import models.*;

import javax.xml.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents the Digital ID object.
 */
@XmlRootElement(name="DigitalID")
@XmlAccessorType(XmlAccessType.FIELD)
public class DigitalID implements WebUpdater
{
    @XmlTransient
    private String sessionName;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public ArrayList<Host> getHosts() {
        return hosts;
    }
    public ArrayList<Inserv> getInservs() {
        return inservs;
    }
    public String getName() {
        return name;
    }

    @XmlElement(name="hosts")
    private ArrayList<Host> hosts;

    @XmlElement(name="inservs")
    private ArrayList<Inserv> inservs;

    public ArrayList<Switch> getSwitches() {
        return switches;
    }

    @XmlElement(name="switches")
    private ArrayList<Switch> switches;

    private String name;

    @XmlTransient
    private String basePath = "C:/digital_ids/";

    @XmlTransient
    private String pathToDigitalIDFile;

    @XmlTransient
    private String pathToXmlFile;

    public String getPathToXmlFile() {
        return pathToXmlFile;
    }

    public String getPathToDigitalIDFile() {
        return pathToDigitalIDFile;
    }

    @XmlTransient
    private String pathToFolder;

    public String getPathToFolder() {
        return pathToFolder;
    }

    @XmlTransient
    private Logger logger;

    /**
     * Empty constructor requrired by JAXB
     */
    public DigitalID(){}

    /***
     * Creates a new instance of the DigitalID object.
     * @param name - name of this digital id.
     * @param hosts - ArrayList of host objects.
     * @param inservs - ArrayList of inserv objects.
     * @param switches - ArrayList of switch objects.
     */
    public DigitalID(String name, ArrayList<Host> hosts, ArrayList<Inserv> inservs, ArrayList<Switch> switches, String sessionName)
    {
        this.name = name;
        this.hosts = hosts;
        this.inservs = inservs;
        this.switches = switches;
        this.sessionName = sessionName;

        initialisePath();
    }

    public DigitalID(String name, String sessionName)
    {
        this.name = name;
        this.sessionName = sessionName;
        initialisePath();
    }

    private void initialisePath()
    {
        pathToDigitalIDFile = basePath + name + "/" + name + ".digitalid";
        pathToXmlFile = basePath + name + "/" + name + ".xml";
        pathToFolder = basePath + name;
        logger = new Logger(pathToDigitalIDFile);
    }

    public void addHosts(ArrayList<Host> hosts)
    {
        if(this.hosts == null)
            this.hosts = new ArrayList<>();

        this.hosts.addAll(hosts);
    }

    public void addHost(Host host)
    {
        if(this.hosts == null)
            this.hosts = new ArrayList<>();

        this.hosts.add(host);
    }

    public void addSwitches(ArrayList<Switch> switches)
    {
        if(this.switches == null)
            this.switches = new ArrayList<>();

        this.switches.addAll(switches);
    }

    public void addSwitch(Switch s)
    {
        if(this.switches == null)
            this.switches = new ArrayList<>();

        this.switches.add(s);
    }

    public void addInservs(ArrayList<Inserv> inservs)
    {
        if(this.inservs == null)
            this.inservs = new ArrayList<>();

        this.inservs.addAll(inservs);
    }

    public void addInserv(Inserv inserv)
    {
        if(this.inservs == null)
            this.inservs = new ArrayList<>();

        this.inservs.add(inserv);
    }

    public void initialise()
    {
        System.out.println("DigitalID " + this.name + " will be created in: " + this.pathToDigitalIDFile);

        if(!name.isEmpty())
        {
            createDirectory(basePath + name);
            logger.initialise();
        }
    }

    public void buildDigitalID() throws IOException, JSchException
    {
        collectHostInformation();

        //collectInservInformation();
        //collectFabricInformation();
    }

    private void collectHostInformation() throws IOException, JSchException
    {
        //logger.addTag("Hosts");
        //logger.openTag();
        if(hosts != null && hosts.size() > 0)
        {
            for(Host host : hosts)
            {
                //logger.addTag(host.getHostName());
                //logger.openTag();
                host.setWebUpdater(this);
                host.connect();
                //host.prepare();

                host.runCommands();

                //host.cleanupHost();
                host.disconnect();
                //logger.closeTag();
                //logger.removeTag();
            }
        }

        //logger.closeTag();
        //logger.removeTag();
    }

    private void collectInservInformation() throws IOException, JSchException
    {
        //logger.addTag("Inservs");
        //logger.openTag();

        if(inservs != null && inservs.size() > 0)
        {
            for(Inserv inserv : inservs)
            {

                //logger.addTag(inserv.getHostName());
                //logger.openTag();

                inserv.connect();
                inserv.setWebUpdater(this);
                if(hosts != null && hosts.size() > 0)
                {
                    for(Host host : hosts)
                    {
                        inserv.retrieveHostDetailedInfo(host.getHostName());
                        inserv.retrieveHostLesbInfo(host.getHostName());
                        inserv.retrieveHostPathSumInfo(host.getHostName());
                    }
                }
                inserv.disconnect();

                //logger.closeTag();
                //logger.removeTag();
            }
        }

        //logger.closeTag();
        //logger.removeTag();
    }

    private void collectFabricInformation()  throws IOException, JSchException
    {
        //logger.addTag("Fabric");
        //logger.openTag();

        if(switches != null && switches.size() > 0)
        {
            for(Switch s : switches)
            {
                //logger.addTag(s.getHostName());
                //logger.openTag();

                s.setWebUpdater(this);
                s.connect();
                s.retrieveSwitchInformation();
                s.retrieveFlogiDatabase();
                s.retrievePortInformation();
                s.disconnect();

                //logger.closeTag();
                //logger.removeTag();
            }
        }

        //logger.closeTag();
        //logger.removeTag();
    }

    private String createDirectory(String path)
    {
        File f = new File(path);

        if(f.isDirectory())
        {
            return f.getAbsolutePath();
        }

        boolean success = f.mkdirs();

        if(!success)
        {
            try {
                throw new Exception("Failed to create directory!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return f.getAbsolutePath();
    }

    @Override
    public void update(WebUpdate webUpdate) {
        DigitalIdController.updateWebInterface(sessionName, webUpdate);
    }


}
