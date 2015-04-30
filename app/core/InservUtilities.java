package core;

/**
 * Created by Michal on 13/12/14.
 */
public class InservUtilities
{
    /*public static void testMasterNode(Inserv inserv, int iterations, int wait, TestType testType)
    {
        for(int i = 0; i < iterations; i++)
        {
            for(int j = 0; j < inserv.getInserv_nodes().length; j++)
            {
                System.out.println("Performing test iteration " + i+1 + " out of " + iterations);

                System.out.println("Connecting to: " + inserv.getInserv_nodes()[j]);

                TelnetClient telnet = new TelnetClient(inserv.getInserv_nodes()[j], "root", "ssmssm");

                System.out.println("Connected to: " + inserv.getInserv_nodes()[j]);

                String masterNodeString = telnet.sendCommand("clwait;");

                int currentNode = Integer.parseInt(masterNodeString.substring(masterNodeString.indexOf("Node=")+5,masterNodeString.indexOf("Node=")+6));
                int masterNode = Integer.parseInt(masterNodeString.substring(masterNodeString.indexOf("Master=")+7,masterNodeString.indexOf("Master=")+8));

                System.out.println("this is current node:" + currentNode);
                System.out.println("this is master node:" + masterNode);

                if(currentNode != masterNode)
                {
                    System.out.println("Testing node "+masterNode);

                    if(testType == TestType.Panic)
                    {
                        telnet.sendCommand("date; cd ~dmcsorle/Testing/Scripts; ./panicnode.sh " + masterNode);
                    }
                    else
                    {
                        telnet.sendCommand("date; shutdownnode reboot -f " + masterNode);
                    }


                    System.out.println("Node "+masterNode+" successfully tested.");
                    break;
                }

                telnet.disconnect();
            }

            try
            {
                System.out.println("Current system time: " + Calendar.getInstance().getTime());
                System.out.println("Sleeping for 15 mins.");
                Thread.sleep(wait);
            } catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }

        }
    }*/
}
