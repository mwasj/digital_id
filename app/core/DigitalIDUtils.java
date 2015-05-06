package core;

import com.google.gson.Gson;
import models.*;
import org.joda.time.DateTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wasinski on 17/02/2015.
 */
public class DigitalIDUtils
{
    public static void marshall(DigitalID digitalID)
    {
        try {
            JAXBContext contextA = JAXBContext.newInstance(DigitalID.class, Switch.class, CiscoSwitch.class, Inserv.class, Host.class, Connectable.class, BrocadeSwitch.class, CommandResponse.class, DateTime.class, Command.class);
            Marshaller marshaller = contextA.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty("jaxb.encoding", "UTF-8");
            File file = new File(digitalID.getPathToXmlFile());
            marshaller.marshal(digitalID, file );
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static DigitalID unMarshall(String path)
    {
        File file = new File(path);

        if(!file.exists())
            return null;

        try {
            JAXBContext context = JAXBContext.newInstance(DigitalID.class, Switch.class, CiscoSwitch.class, Inserv.class, Host.class, Connectable.class, BrocadeSwitch.class, CommandResponse.class, DateTime.class, Command.class);
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            return  (DigitalID) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

       return null;
    }

    public static ArrayList<String> list()
    {
        File f = new File("C:\\digital_ids");
        return new ArrayList<>(Arrays.asList(f.list()));
    }
}
