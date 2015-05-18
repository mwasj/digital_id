package core;

import com.jcraft.jsch.*;
import models.*;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import java.io.*;

public class ConnectionManager {

    private Session session;
    private boolean isOpen;
    private Connectable connectable;
    private int channelCounter;
    private int channelMax;

    public Session getSession() {
        return session;
    }

    public Connectable getConnectable() {
        return connectable;
    }

    public ConnectionManager(Connectable connectable)
    {
        this.connectable = connectable;

        if(connectable instanceof CiscoSwitch || connectable instanceof BrocadeSwitch)
        {
            channelMax = 2;
        }
        else
        {
            channelMax  = 999;
        }
    }


    public void connect()
    {
        session = createSession();
        establishSession(false);
    }

    /***
     *
     * @param command
     * @param remoteCommandType
     * @return
     * @throws IOException
     * @throws JSchException
     */
    public CommandResponse sendCommand(String command, RemoteCommandType remoteCommandType) throws IOException, JSchException
    {
        long id = DigitalIDUtils.getUniqueID();

        /*if(command.isCausingWebUpdate())
        {
            connectable.getWebUpdater().sendUpdate(new WebUpdate("Executing command: " + command.getCommand(), id, null, WebUpdateType.progressUpdate));
        }*/

        CommandResponse commandResponse = null;

        if(session == null)
        {
            session = createSession();
        }

        if(!session.isConnected())
        {
            channelCounter = 0;
            //connectable.getWebUpdater().sendUpdate(new WebUpdate("Connection interrupted, reconnecting  to: " + connectable.getHostName(), id, null, WebUpdateType.progressUpdate));
            establishSession(false);
        }

        if(remoteCommandType == RemoteCommandType.Exec)
        {
            commandResponse = sendExecCommand(command);
        }
        else if (remoteCommandType == RemoteCommandType.Shell)
        {
            commandResponse = sendShellCommand(command);
        }

        /*if(command.isCausingWebUpdate())
        {
            connectable.getWebUpdater().sendUpdate(new WebUpdate(null, id, commandResponse, WebUpdateType.progressUpdate));
        }*/

        return commandResponse;
    }

    private CommandResponse<String> sendShellCommand(String command) throws JSchException, IOException
    {
        String errorMessage = "";
        Channel channel = session.openChannel("shell");
        OutputStream outputStream = channel.getOutputStream();
        PrintStream commander = new PrintStream(outputStream, true);

        channel.connect();
        commander.println(command);
        commander.println("exit");
        commander.close();

        InputStream inputStream = channel.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String result = IOUtils.toString(bufferedReader).replaceAll("\0", "").replaceAll("�", "").replaceAll("ÿþ","");;

        CommandResponseCode commandResponseCode = waitForCommandToFinish(channel);

        outputStream.close();
        inputStream.close();
        channel.disconnect();
        channel.getInputStream().close();
        channel.getOutputStream().close();

        return new CommandResponse<>(result, commandResponseCode, errorMessage);
    }

    private CommandResponse<String> sendExecCommand(String command) throws JSchException, IOException
    {
        String errorMessage = "";
        channelCounter += 1;

        if(channelCounter == channelMax)
        {
            System.out.println("Channel limit reached, resetting connection...");
            session.disconnect();
            session = null;
            session = createSession();
            establishSession(true);
            channelCounter = 0;
        }

        Channel channel=session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);

        channel.connect();

        InputStream inputStream=channel.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String result = IOUtils.toString(reader).replaceAll("\0", "").replaceAll("�", "").replaceAll("ÿþ","");

        CommandResponseCode commandResponseCode = waitForCommandToFinish(channel);

        inputStream.close();
        reader.close();
        channel.getInputStream().close();
        channel.getOutputStream().close();
        channel.disconnect();

        return new CommandResponse(result, commandResponseCode, errorMessage);
    }

    public CommandResponse<File> getFile(String remoteFile, String localFile)
    {
        FileOutputStream fos=null;
        String errorMessages = "";
        CommandResponseCode commandResponseCode = CommandResponseCode.Success;

        try{

            String prefix=null;

            if(new File(localFile).isDirectory())
            {
                prefix=localFile+File.separator;
            }

            // exec 'scp -f rfile' remotely
            String command="scp -f "+remoteFile;

            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out=channel.getOutputStream();
            InputStream in=channel.getInputStream();

            channel.connect();

            byte[] buf=new byte[1024];

            // send '\0'
            buf[0]=0; out.write(buf, 0, 1); out.flush();

            while(true){
                int c=checkAck(in);
                if(c!='C'){
                    break;
                }

                // read '0644 '
                in.read(buf, 0, 5);

                long filesize=0L;

                while(true)
                {
                    if(in.read(buf, 0, 1)<0)
                    {
                        commandResponseCode = CommandResponseCode.Failure;
                        break;
                    }

                    if(buf[0]==' ')break;
                    filesize=filesize*10L+(long)(buf[0]-'0');
                }

                String file;
                for(int i=0;;i++)
                {
                    in.read(buf, i, 1);

                    if(buf[i]==(byte)0x0a)
                    {
                        file=new String(buf, 0, i);
                        break;
                    }
                }

                // send '\0'
                buf[0]=0;
                out.write(buf, 0, 1); out.flush();

                // read a content of lfile
                fos=new FileOutputStream(prefix==null ? localFile : prefix+file);

                int foo;

                while(true)
                {
                    if(buf.length<filesize)
                    {
                        foo=buf.length;
                    }
                    else
                    {
                        foo=(int)filesize;
                    }

                    foo=in.read(buf, 0, foo);

                    if(foo<0)
                    {
                        commandResponseCode = CommandResponseCode.Failure;
                        break;
                    }

                    fos.write(buf, 0, foo);
                    filesize-=foo;

                    if(filesize==0L)
                    {
                        break;
                    }
                }

                fos.close();
                fos=null;

                if(checkAck(in)!=0)
                {
                    commandResponseCode = CommandResponseCode.Failure;
                    return null;
                }

                // send '\0'
                buf[0]=0;
                out.write(buf, 0, 1);
                out.flush();
            }
        }
        catch(Exception e)
        {
            errorMessages += e;
            System.out.println(e);

            try
            {
                if(fos!=null)
                    fos.close();
            }
            catch(Exception ee)
            {
                errorMessages += ee;
                System.out.println(ee);
                commandResponseCode = CommandResponseCode.Failure;
            }
        }

        return new CommandResponse<>(new File(localFile), commandResponseCode, errorMessages);
    }

    public CommandResponse readFile(String remoteFile)
    {
        CommandResponseCode commandResponseCode = CommandResponseCode.Success;
        String errorMessage = "";

        String result = "";
        try
        {
            // exec 'scp -f rfile' remotely
            String command="scp -f "+remoteFile;

            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream outputStream=channel.getOutputStream();
            InputStream inputStream=channel.getInputStream();

            channel.connect();

            byte[] bytes=new byte[1024];

            // send '\0'
            bytes[0]=0; outputStream.write(bytes, 0, 1); outputStream.flush();

            while(true){
                int c=checkAck(inputStream);
                if(c!='C'){
                    break;
                }

                // read '0644 '
                inputStream.read(bytes, 0, 5);

                long filesize=0L;
                while(true){
                    if(inputStream.read(bytes, 0, 1)<0){
                        // error
                        break;
                    }
                    if(bytes[0]==' ')break;
                    filesize=filesize*10L+(long)(bytes[0]-'0');
                }

                for(int i=0;;i++){
                    inputStream.read(bytes, i, 1);
                    if(bytes[i]==(byte)0x0a){
                        break;
                    }
                }

                // send '\0'
                bytes[0]=0;
                outputStream.write(bytes, 0, 1); outputStream.flush();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                int index;

                while(true)
                {
                    if(bytes.length<filesize)
                    {
                        index=bytes.length;
                    }
                    else
                    {
                        index=(int)filesize;
                    }

                    index=inputStream.read(bytes, 0, index);

                    if(index<0)
                    {
                        commandResponseCode = CommandResponseCode.Failure;
                        break;
                    }

                    byteArrayOutputStream.write(bytes, 0, index);
                    result = new String(byteArrayOutputStream.toByteArray()).replaceAll("\0", "").replaceAll("�", "");
                    filesize-=index;

                    if(filesize==0L)
                    {
                        break;
                    }
                }

                if(checkAck(inputStream)!=0)
                {
                    commandResponseCode = CommandResponseCode.Failure;
                    return null;
                }

                // send '\0'
                bytes[0]=0;
                outputStream.write(bytes, 0, 1);
                outputStream.flush();

            }
        }
        catch(Exception e)
        {
            errorMessage += e;
            commandResponseCode = CommandResponseCode.Failure;
        }

        return new CommandResponse(result, commandResponseCode, errorMessage);
    }

    /**
     * Converts a file to a stream of bytes and sends it to the remote host.
     * @param localFile - local file to be sent.
     * @param remoteFile - file to be written to on the remote host.
     */
    public CommandResponse<Void> sendFile(String localFile, String remoteFile)
    {
        FileInputStream fis=null;
        String errorMessages = "";
        CommandResponseCode commandResponseCode = CommandResponseCode.Success;

        try
        {

            boolean ptimestamp = false;

            // exec 'scp -t rfile' remotely
            String command="scp " + (ptimestamp ? "-p" :"") +" -t "+remoteFile;
            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out=channel.getOutputStream();
            InputStream in=channel.getInputStream();

            channel.connect();

            if(checkAck(in)!=0){
                commandResponseCode = CommandResponseCode.Failure;
            }

            File _lfile = new File(localFile);

            if(ptimestamp)
            {
                command="T "+(_lfile.lastModified()/1000)+" 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command+=(" "+(_lfile.lastModified()/1000)+" 0\n");
                out.write(command.getBytes()); out.flush();

                if(checkAck(in)!=0)
                {
                    commandResponseCode = CommandResponseCode.Failure;
                }
            }

            // send "C0644 filesize filename", where filename should not include '/'
            long filesize=_lfile.length();
            command="C0644 "+filesize+" ";

            if(localFile.lastIndexOf('/')>0)
            {
                command+=remoteFile.substring(localFile.lastIndexOf('/')+1);
            }
            else
            {
                command+=localFile;
            }
            command+="\n";
            out.write(command.getBytes()); out.flush();

            if(checkAck(in)!=0)
            {
                commandResponseCode = CommandResponseCode.Failure;
            }

            // send a content of lfile
            fis=new FileInputStream(localFile);
            byte[] buf=new byte[1024];

            while(true)
            {
                int len=fis.read(buf, 0, buf.length);
                if(len<=0) break;
                out.write(buf, 0, len); //out.flush();
            }
            fis.close();
            fis=null;
            // send '\0'
            buf[0]=0; out.write(buf, 0, 1); out.flush();
            if(checkAck(in)!=0)
            {
                commandResponseCode = CommandResponseCode.Failure;
            }
            out.close();
        }
        catch(Exception e)
        {
            commandResponseCode = CommandResponseCode.Failure;
            System.out.println(e);
            errorMessages += e;
            try
            {
                if(fis!=null)
                    fis.close();
            }
            catch(Exception ee){}
        }

        return new CommandResponse<>(null, commandResponseCode, errorMessages);
    }

    private static int checkAck(InputStream in) throws IOException{
        int b=in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if(b==0) return b;
        if(b==-1) return b;

        if(b==1 || b==2){
            StringBuffer sb=new StringBuffer();
            int c;
            do {
                c=in.read();
                sb.append((char)c);
            }
            while(c!='\n');
            if(b==1){ // error
                System.out.print(sb.toString());
            }
            if(b==2){ // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

    private CommandResponseCode waitForCommandToFinish(Channel channel)
    {
        while(!(channel.getExitStatus() >= 0))
        {
            try
            {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return channel.getExitStatus() == 0 ? CommandResponseCode.Success : CommandResponseCode.Failure;
    }

    private Session createSession()
    {
        Session s = null;
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        JSch jsch = new JSch();

        try {
            s = jsch.getSession(connectable.getUserName(), connectable.getHostName(), 22);
            s.setConfig(config);
            s.setPassword(connectable.getPassword());
        } catch (JSchException e)
        {
            System.out.println("Error, could not create session!");
            e.printStackTrace();
        }

        return s;
    }

    /**
     * Responsible for establishing session to the host.
     */
    private void establishSession(boolean reset)
    {
        long id =  DigitalIDUtils.getUniqueID();
        DateTime startDate = DateTime.now();

        if(session == null)
        {
            session = createSession();
        }

        if(session.isConnected())
        {
            //Already connected.
            return;
        }

        try
        {
            System.out.println(connectable == null);
            System.out.println(connectable.getWebUpdater() == null);

            //Only send an sendUpdate if a brand new connection is being established.
            if(!reset)
                //connectable.getWebUpdater().sendUpdate(new WebUpdate("Connecting to: " + connectable.getHostName(), id, null, WebUpdateType.progressUpdate));

            session.connect();

            if(session.isConnected())
            {
                /*if(!reset)
                {
                    connectable.getWebUpdater().sendUpdate(new WebUpdate(null, id,
                            new CommandResponse("Successfully connected to: " + connectable.getHostName(), CommandResponseCode.Success, null, null, startDate, DateTime.now()), WebUpdateType.progressUpdate));
                }*/

            }
        }
        catch (JSchException e)
        {
            /*if(!reset)
            {
                connectable.getWebUpdater().sendUpdate(new WebUpdate(null, id,
                        new CommandResponse("Could not connect to: " + connectable.getHostName(), CommandResponseCode.Failure, null, null, startDate, DateTime.now()), WebUpdateType.progressUpdate));
            }*/

            e.printStackTrace();
        }
    }
    /**
     * Close the connection.
     */
    public void close()
    {
        if (this.isOpen)
        {
            this.session.disconnect();
            this.session = null;
        }

        this.isOpen = false;
    }
}