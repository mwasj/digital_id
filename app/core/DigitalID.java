package core;

import com.jcraft.jsch.JSchException;
import controllers.DigitalIdController;
import interfaces.WebUpdater;
import models.*;

import javax.xml.bind.annotation.*;
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

    private String author;

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

    public String getAuthor() {
        return author;
    }

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
    public DigitalID(String author, String name, ArrayList<Host> hosts, ArrayList<Inserv> inservs, ArrayList<Switch> switches, String sessionName)
    {
        this.author = author;
        this.name = name+"_"+author;
        this.hosts = hosts;
        this.inservs = inservs;
        this.switches = switches;
        this.sessionName = sessionName;

        initialisePath();
    }

    public DigitalID(String author, String name, String sessionName)
    {
        this.author = author;
        this.name = name+"_"+author;
        this.sessionName = sessionName;

        initialisePath();
    }

    private void initialisePath()
    {
        pathToDigitalIDFile = basePath + "/" + name + ".digitalid";
        pathToXmlFile = basePath  + "/" + name + ".xml";
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

    public void buildDigitalID() throws IOException, JSchException
    {
        if(hosts != null && hosts.size() > 0)
        {
            for(Host host : hosts)
            {
                host.setWebUpdater(this);
                host.connect();
                host.runCommands();
                host.disconnect();

            }
        }

        if(switches != null && switches.size() > 0)
        {
            for(Switch s : switches)
            {
                s.setWebUpdater(this);
                s.connect();
                s.runCommands();
                s.disconnect();
            }
        }

        if(inservs != null && inservs.size() > 0)
        {
            for(Inserv inserv : inservs)
            {
                System.out.println(inserv.getHostName());
                inserv.setWebUpdater(this);
                inserv.connect();
                inserv.runCommands();
                inserv.disconnect();
            }
        }

        DigitalIDUtils.marshall(this);

        sendUpdate(new WebUpdate(this.getName(), 0, null, WebUpdateType.finish));
    }


    @Override
    public void sendUpdate(WebUpdate webUpdate) {
        DigitalIdController.updateWebInterface(sessionName, webUpdate);
    }


}
