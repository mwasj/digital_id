package core;

import models.*;

import java.util.ArrayList;

/**
 * Created by wasinski on 10/02/2015.
 */
public class DigitalIdComparator
{
    private DigitalID digitalID1;
    private DigitalID digitalID2;
    private ArrayList<Accordion> accordions;

    public ArrayList<Accordion> getAccordions() {
        return accordions;
    }

    public DigitalIdComparator(DigitalID digitalID1, DigitalID digitalID2)
    {
        this.digitalID1 = digitalID1;
        this.digitalID2 = digitalID2;
        this.accordions = new ArrayList<>();
    }

    public void compareHosts()
    {
        Accordion hostAccordion = new Accordion("Hosts", "Hosts", null, null, 0);

        for(Host host : digitalID1.getHosts())
        {
            for(Host host2 : digitalID2.getHosts())
            {
                if(host.getHostName().equals(host2.getHostName()))
                {
                    Accordion accordion = hostAccordion.addSubAccordion(new Accordion(host.getHostName(), host.getHostName(), null, null, 0));

                    String hostInfo = host.getContext().applyDiffFilters(host.getSystemInformation());
                    String hostInfo2 = host2.getContext().applyDiffFilters(host2.getSystemInformation());

                    accordion.addSubAccordion(new Accordion("System Information",
                            createDivName(host, "SystemInfo"),
                            hostInfo,
                            hostInfo2,
                            levenshteinDistance(hostInfo, hostInfo2)));

                    String multiPathInfo = host.getContext().applyDiffFilters(host.getMultipathInformation());
                    String multiPathInfo2 = host2.getContext().applyDiffFilters(host2.getMultipathInformation());

                    accordion.addSubAccordion(new Accordion("Multipath Information",
                            createDivName(host, "MultipathInfo"),
                            multiPathInfo, multiPathInfo2,
                            levenshteinDistance(multiPathInfo, multiPathInfo2)));

                    String sg3utils1 = host.getContext().applyDiffFilters(host.getSg3utilsInformation());
                    String sg3utils2 = host2.getContext().applyDiffFilters(host2.getSg3utilsInformation());

                    int difference = levenshteinDistance(sg3utils1, sg3utils2);

                    accordion.addSubAccordion(new Accordion("Sg3Utils Information",
                            createDivName(host,"sg3utils"),
                            sg3utils1,
                            sg3utils2,
                            difference));
                }
            }
        }

        accordions.add(hostAccordion);
    }

    public void compareInservs()
    {
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
        return s+connectable.getHostName();
    }
}
