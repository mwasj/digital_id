package core;

import dtos.ContentDto;
import models.*;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wasinski on 10/02/2015.
 */
public class DigitalIdComparator
{
    private DigitalID digitalID1;
    private DigitalID digitalID2;
    private ArrayList<Accordion> accordions;
    private ArrayList<ContentDto> contentDtos;

    public ArrayList<ContentDto> getContentDtos() {
        return contentDtos;
    }

    public ArrayList<Accordion> getAccordions() {
        return accordions;
    }

    public DigitalIdComparator(DigitalID digitalID1, DigitalID digitalID2)
    {
        this.digitalID1 = digitalID1;
        this.digitalID2 = digitalID2;
        this.accordions = new ArrayList<>();
        this.contentDtos = new ArrayList<>();
    }

    public void compareHosts()
    {
        if(digitalID1.getHosts() != null || digitalID2.getHosts() != null)
        {
            buildAccordion(digitalID1.getHosts(), digitalID2.getHosts(), "Hosts");
        }

        if(digitalID1.getSwitches() != null || digitalID2.getSwitches() != null)
        {
            buildAccordion(digitalID1.getSwitches(), digitalID2.getSwitches(), "Switches");
        }

        if(digitalID1.getInservs() != null || digitalID2.getInservs() != null)
        {
            buildAccordion(digitalID1.getInservs(), digitalID2.getInservs(), "Arrays");
        }
    }

    private <T> void buildAccordion (ArrayList<T> array1, ArrayList<T> array2, String title)
    {
        Accordion mainAccordion = new Accordion(title, title, null, null, 0);

        for(Connectable connectable : (ArrayList<Connectable>) array1)
        {
            for(Connectable connectable2 : (ArrayList<Connectable>) array2)
            {

                if(connectable.getHostName().equals(connectable2.getHostName()))
                {
                    for(CommandResponse commandResponse : connectable.getCommandResponses())
                    {
                        for(CommandResponse commandResponse1 : connectable2.getCommandResponses())
                        {
                            if(commandResponse.getCommand().getCommand().equals(commandResponse1.getCommand().getCommand()) &&
                                    commandResponse.getCommand().isComparable() == commandResponse1.getCommand().isComparable())
                            {
                                Accordion accordion = mainAccordion.addSubAccordion(new Accordion(connectable.getHostName(), connectable2.getHostName(), null, null, 0));

                                String divName = createDivName(connectable, commandResponse.getCommand().getCommand() + Calendar.getInstance().get(Calendar.MILLISECOND));

                                accordion.addSubAccordion(new Accordion(commandResponse.getCommand().getCommand(),
                                        divName,
                                        commandResponse.getResult(),
                                        commandResponse1.getResult(),
                                        levenshteinDistance(commandResponse.getResult(), commandResponse1.getResult())));

                                contentDtos.add(new ContentDto( commandResponse.getResult(), commandResponse1.getResult(), divName));
                            }
                        }
                    }
                }
            }
        }

        accordions.add(mainAccordion);
    }

    public void compareInservs()
    {
        if(digitalID1.getInservs() == null || digitalID2.getInservs() == null)
        {
            return;
        }

        Accordion inservAccordion = new Accordion("Storage Arrays", "StorageArrays",null, null, 0);

        for(Inserv inserv : digitalID1.getInservs())
        {
            for(Inserv inserv1 : digitalID2.getInservs())
            {
                if(inserv.getHostName().equals(inserv1.getHostName()))
                {
                    Accordion accordion = inservAccordion.addSubAccordion(new Accordion(inserv.getHostName(), createDivName(inserv, "storeserv"), null, null, 0));

                    String hostInfo = inserv.getContext().applyDiffFilters(inserv.getHostDetailedInformation());
                    String hostInfo2 = inserv1.getContext().applyDiffFilters(inserv1.getHostDetailedInformation());
                    accordion.addSubAccordion(new Accordion("Showhost -d", createDivName(inserv, "showhostd"), hostInfo, hostInfo2, levenshteinDistance(hostInfo, hostInfo2)));

                    String hostLesbInfo = inserv.getContext().applyDiffFilters(inserv.getHostLesbInformation());
                    String hostLesbInfo2 = inserv1.getContext().applyDiffFilters(inserv1.getHostLesbInformation());
                    accordion.addSubAccordion(new Accordion("Showhost -lesb", createDivName(inserv, "showhostlesb"), hostLesbInfo, hostLesbInfo2, levenshteinDistance(hostLesbInfo, hostLesbInfo2)));

                    String hostPathSumInfo = inserv.getContext().applyDiffFilters(inserv.getHostPathSumInformation());
                    String hostPathSumInfo2 = inserv1.getContext().applyDiffFilters(inserv1.getHostPathSumInformation());
                    accordion.addSubAccordion(new Accordion("Showhost -pathsum", createDivName(inserv, "showhostpathsum"), hostPathSumInfo, hostPathSumInfo2, levenshteinDistance(hostPathSumInfo, hostPathSumInfo2)));
                }
            }
        }

        accordions.add(inservAccordion);
    }

    public void compareSwitches()
    {
        if(digitalID1.getSwitches() == null || digitalID2.getSwitches() == null)
        {
            return;
        }

        Accordion switchAccordion = new Accordion("Fabric", "Fabric", null, null, 0);

        for(Switch switch1 : digitalID1.getSwitches())
        {
            for(Switch switch2 : digitalID2.getSwitches())
            {
                if(switch1.getHostName().equals(switch2.getHostName()))
                {
                    Accordion accordion = switchAccordion.addSubAccordion(new Accordion(switch1.getHostName(), switch1.getHostName(),null, null, 0));
                    System.out.println(switch1.getHostName());
                    String switchInfo = switch1.getContext().applyDiffFilters(switch1.getSwitchInformation());
                    String switchInfo2 = switch2.getContext().applyDiffFilters(switch2.getSwitchInformation());
                    accordion.addSubAccordion(new Accordion("System Information", createDivName(switch1, "sysinfo"), switchInfo, switchInfo2, levenshteinDistance(switchInfo,switchInfo2)));

                    String flogiInfo = switch1.getContext().applyDiffFilters(switch1.getFlogiDatabase());
                    String flogiInfo2 = switch2.getContext().applyDiffFilters(switch2.getFlogiDatabase());

                    accordion.addSubAccordion(new Accordion("Flogi Database", createDivName(switch1, "flogidatabase"), flogiInfo, flogiInfo2, levenshteinDistance(flogiInfo,flogiInfo2)));

                    Accordion activePortsHeaderAccordion = accordion.addSubAccordion(new Accordion("Active Ports", createDivName(switch1, "activeports"), null, null,0));

                    for(SwitchPort port1 : switch1.getActivePorts())
                    {
                        for(SwitchPort port2 : switch2.getActivePorts())
                        {
                            if(port1.getPortNumber().equals(port2.getPortNumber()))
                            {
                                String portInfo = switch1.getContext().applyDiffFilters(port1.getPortInformation());
                                String portInfo2 = switch2.getContext().applyDiffFilters(port2.getPortInformation());

                                activePortsHeaderAccordion.addSubAccordion(new Accordion(port1.getPortNumber(), createDivName(switch1, port1.getPortNumber().replaceAll("/", "")), portInfo, portInfo2, levenshteinDistance(portInfo,portInfo2)));
                            }
                        }
                    }
                }
            }
        }

        accordions.add(switchAccordion);
    }

    /**
     * Calculate the difference between two strings and returns
     * an integer containing the number of differences.
     * @param s0 - base string.
     * @param s1 - string to be compared.
     * @return - number of differences between s0 & s1.
     */
    private static int levenshteinDistance(String s0, String s1) {
        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

    private static int lineDifference(String s0, String s1)
    {
        int difference = 0;

        String[] array1 = s0.split("\\r?\\n");
        String[] array2 = s1.split("\\r?\\n");

        return difference;
    }

    private static String createDivName(Connectable connectable, String s)
    {
        return s+connectable.getHostName().replace("-", "");
    }
}
