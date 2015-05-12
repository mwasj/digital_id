package core;

import com.jcraft.jsch.JSchException;
import models.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, JSchException, JAXBException
    {
        //Create the Cisco switch object.
       /* Switch cs9710ha01 = new CiscoSwitch("cs9710-ha-02", "admin", "3pardata", null);

        //Create a list of active ports to be added to the Cisco switch.
        ArrayList<SwitchPort> activePorts = new ArrayList<SwitchPort>();
        activePorts.add(new SwitchPort("fc1/43"));
        activePorts.add(new SwitchPort("fc1/44"));

        //Set active ports.
        cs9710ha01.setActivePorts(activePorts);
        cs9710ha01.setPortSpeed(activePorts.get(1), 16000);

        //Create the Brocade switch object.
        BrocadeSwitch brocadeSwitch = new BrocadeSwitch("br300-ha-1", "root", "ssmssm99", null);

        //Create a list of active ports to be added to the Brocade switch.
        ArrayList<SwitchPort> activePorts2 = new ArrayList<>();
        activePorts2.add(new SwitchPort("1"));
        activePorts2.add(new SwitchPort("2"));

        //Set active ports.
        brocadeSwitch.setActivePorts(activePorts2);

        //This is my new Tornado array.
        Inserv s99302 = new Inserv("s99302", "root", "ssmssm");

        Host dl360g707 = new WindowsHost("dl360g7-07", "Administrator", "ssmssm");

        /*
        These hosts are currently offline.
         */
       /* Host dl380pg873 = new WindowsHost("dl380pg8-73", "Administrator", "ssmssm");
        Host dl380pg874 = new WindowsHost("dl380pg8-74", "Administrator", "ssmssm");
        Host hpc700007b15 = new WindowsHost("hpc7000-07-b15", "Administrator", "ssmssm");

        ArrayList<Host> hosts = new ArrayList<>();
        hosts.add(dl360g707);



        DigitalID digitalID = new DigitalID("test",
                hosts,
                new Inserv[] {s99302}, new Switch[]{cs9710ha01, brocadeSwitch});

        digitalID.initialise();
        digitalID.buildDigitalID();
        DigitalIdMarshaller.marshall(digitalID);


        cs9710ha01.setPortSpeed(activePorts.get(0), 8000);

        DigitalID digitalID1 = new DigitalID("test2",
                hosts,
                new Inserv[] {s99302}, new Switch[]{cs9710ha01, brocadeSwitch});

        digitalID1.initialise();
        digitalID1.buildDigitalID();

        cs9710ha01.setPortSpeed(activePorts.get(1), 16000);
        DigitalIdMarshaller.marshall(digitalID1);


        DigitalID digitalID2 = DigitalIdMarshaller.unMarshall("C:/digital_ids/test/test.xml");
        DigitalID digitalID3 = DigitalIdMarshaller.unMarshall("C:/digital_ids/test2/test2.xml");

        DigitalIdComparator comparator = new DigitalIdComparator(digitalID2, digitalID3);
        comparator.compare();
        comparator.compareInservs();
        comparator.compareSwitches();

        HtmlGenerator generator = new HtmlGenerator(comparator.getAccordions());
        generator.generateHtml("C:\\digital_ids\\html\\"+digitalID2.getName()+"_"+digitalID3.getName()+".html");*/
    }
}