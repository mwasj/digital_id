package core;

import com.google.gson.Gson;
import models.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

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

    public static ArrayList<DigitalIdFileAttributes> list()
    {
        //List & filter all files in the base directory
        File baseDirectory = new File("C:\\digital_ids");
        File[] files = baseDirectory.listFiles(new FilenameFilter()
               {
                   public boolean accept(File dir, String name) {
                       return name.toLowerCase().endsWith(".xml");
                   }
               }
        );

        ArrayList<DigitalIdFileAttributes> digitalIdFileAttributeses = new ArrayList<>();

        for(File f : files)
        {
            String p = f.getAbsolutePath();
            Path path = Paths.get(p);
            BasicFileAttributes attributes = null;

            try {
                attributes = Files.readAttributes(path, BasicFileAttributes.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(attributes != null)
            {
                DateTime date = new DateTime(attributes.creationTime().toMillis());
                String formattedDate = date.toString("dd-MMM-yy HH:mm:ss", Locale.getDefault());

                String author = f.getName().contains("_") ? f.getName().substring(f.getName().lastIndexOf("_")+1, f.getName().length()-4) : null;
                String url = f.getName();
                String displayName = author == null ? f.getName() : f.getName().substring(0, f.getName().lastIndexOf("_"));

                digitalIdFileAttributeses.add(
                        new DigitalIdFileAttributes(author, url,displayName,
                        formattedDate, String.valueOf(attributes.size()/1024)));
            }
        }

        return digitalIdFileAttributeses;
    }

    public static long getUniqueID()
    {
        return Calendar.getInstance().getTimeInMillis();
    }
}

