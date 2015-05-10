package core;

import dtos.ContentDto;
import models.*;

import java.math.BigInteger;
import java.security.SecureRandom;
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
                    String divName = createDivName(connectable);

                    Accordion accordion = mainAccordion.addSubAccordion(new Accordion(connectable.getHostName(), divName, null, null, 0));

                    for(CommandResponse commandResponse : connectable.getCommandResponses())
                    {
                        for(CommandResponse commandResponse1 : connectable2.getCommandResponses())
                        {
                            if(commandResponse.getCommand().getCommand().equals(commandResponse1.getCommand().getCommand()) &&
                                    (commandResponse.getCommand().isComparable() && commandResponse1.getCommand().isComparable()))
                            {
                                String divName2 = createDivName(connectable);

                                accordion.addSubAccordion(new Accordion(commandResponse.getCommand().getCommand(),
                                        divName2,
                                        commandResponse.getResult(),
                                        commandResponse1.getResult(),
                                        levenshteinDistance(commandResponse.getResult(), commandResponse1.getResult())));

                                contentDtos.add(new ContentDto( commandResponse.getResult(), commandResponse1.getResult(), divName2));
                            }
                        }
                    }
                }
            }
        }

        accordions.add(mainAccordion);
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

    private static String createDivName(Connectable connectable)
    {
        long timeMilli2 =  Calendar.getInstance().getTimeInMillis();
        return connectable.getHostName().replaceAll("-", "")+timeMilli2+nextDiveName();
    }

    public static String nextDiveName()
    {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
