package core;

import com.google.gson.Gson;
import models.*;
import org.joda.time.DateTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by wasinski on 17/02/2015.
 */
public class DigitalIdMarshaller
{
    public static void marshall(DigitalID digitalID)
    {

        System.out.println(digitalID.getHosts().get(0).getCommandResponses().get(0).getExecutionStartTime());
        JAXBContext contextA = null;
        try {
            contextA = JAXBContext.newInstance(DigitalID.class, Switch.class, CiscoSwitch.class, Inserv.class, Host.class, Connectable.class, BrocadeSwitch.class, CommandResponse.class, DateTime.class, Command.class);
            Marshaller marshaller = contextA.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty("jaxb.encoding", "Unicode");
            File file = new File(digitalID.getPathToFolder()+"\\"+digitalID.getName()+".xml");
            marshaller.marshal(digitalID, file );
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static DigitalID unMarshall(String path)
    {
        DigitalID digitalID = null;
        File file = new File(path);
        JAXBContext context = null;

        try {
            context = JAXBContext.newInstance(DigitalID.class, Switch.class, CiscoSwitch.class, Inserv.class, Host.class, Connectable.class, BrocadeSwitch.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            digitalID =  (DigitalID) jaxbUnmarshaller.unmarshal(file);
            return  digitalID;
        } catch (JAXBException e) {
            e.printStackTrace();
        }

       return digitalID;
    }
}
