package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by wasinski on 05/02/2015.
 */
public class Logger
{
    private File file;
    private LinkedList<String> tags;
    private BufferedWriter bufferedWriter;

    public String getCurrentTag() {
        return tags.getLast();
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag()
    {
        if(tags.size() > 0)
        {
            tags.removeLast();
        }
    }

    public Logger(String path)
    {
        file =  new File(path);
        tags = new LinkedList<>();
    }

    public void initialise()
    {
        try
        {
            if(file.exists())
            {
                file.delete();
                file.createNewFile();
            }

            bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addContent(String content) throws IOException
    {
        try
        {
            if(bufferedWriter == null)
            {
                System.out.println("Logger has not been initianised properly, make sure you call logger.initialise()");
                return;
            }

            bufferedWriter.write("\n" + content + "\n");
            bufferedWriter.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void openTag() throws IOException {
        if(tags != null && tags.size() > 0)
        {
            bufferedWriter.write("\n" + "#Open " + tags.getLast() + " section#" + "\n");
            bufferedWriter.flush();
        }
        else
        {
            System.out.println("Could not open tag, tag is null or empty. Please use setTag");
        }
    }

    public void closeTag() throws IOException {
        if(tags != null && tags.size() > 0)
        {
            bufferedWriter.write("\n" + "#Close " + tags.getLast() + " section#" + "\n");
            bufferedWriter.flush();
        }
        else
        {
            System.out.println("Could not open tag, tag is null or empty. Please use setTag");
        }
    }

    public void close() throws IOException
    {
        if(bufferedWriter != null)
        {
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }
}
