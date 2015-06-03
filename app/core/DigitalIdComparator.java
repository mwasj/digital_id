package core;

import actions.Action;
import commands.Command;
import dtos.ComparisonContentDTO;
import dtos.ComparisonDTO;
import dtos.ContainerDTO;
import dtos.ContentDto;
import models.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wasinski on 10/02/2015.
 */
public class DigitalIdComparator
{
    private DigitalID digitalID1;
    private DigitalID digitalID2;
    private ArrayList<Accordion> accordions;
    private ArrayList<ContentDto> contentDtos;
    private ComparisonDTO comparisonDTO;

    public DigitalIdComparator(ComparisonDTO comparisonDTO)
    {
        digitalID1 = DigitalIDUtils.unMarshall("C:\\digital_ids\\"+comparisonDTO.getBeforeArray().get(0).getFileName()+".xml");
        digitalID2 = DigitalIDUtils.unMarshall("C:\\digital_ids\\"+comparisonDTO.getAfterArray().get(0).getFileName()+".xml");
        this.comparisonDTO = comparisonDTO;
    }

    public ArrayList<ComparisonContentDTO> performComparison()
    {
        return compare(buildActionList(digitalID1,comparisonDTO.getBeforeArray()), buildActionList(digitalID2,comparisonDTO.getAfterArray()));
    }

    public ArrayList<ContentDto> getContentDtos() {
        return contentDtos;
    }

    public ArrayList<Accordion> getAccordions() {
        return accordions;
    }
    private boolean incompatible;

    public DigitalIdComparator(DigitalID digitalID1, DigitalID digitalID2)
    {
        this.digitalID1 = digitalID1;
        this.digitalID2 = digitalID2;
        this.accordions = new ArrayList<>();
        this.contentDtos = new ArrayList<>();
        this.incompatible = true;
    }

    public void generateNonComparisonReport()
    {
        if(digitalID1.getHosts() != null)
        {
            buildAccordions(digitalID1.getHosts(), "Hosts");
        }

        if(digitalID1.getSwitches() != null)
        {
            buildAccordions(digitalID1.getSwitches(), "Switches");
        }

        if(digitalID1.getInservs() != null)
        {
            buildAccordions(digitalID1.getInservs(), "Arrays");
        }
    }

    private void markIncompatible()
    {
        String message = "Error, these two Digital IDs are incompatible.";
        String divName = "incompatible";
        Accordion mainAccordion = new Accordion(message, divName,-1,null);
        accordions.add(mainAccordion);
       // contentDtos.add(new ContentDto(message, null, divName));
    }

    private ArrayList<Action> buildActionList(DigitalID digitalId, ArrayList<ContainerDTO> containers)
    {
        ArrayList<Action> actions = new ArrayList<>();

        for(ContainerDTO containerDTO : containers) {
            for (Connectable connectable : digitalId.getConnectables()) {
                for (Action action : connectable.getActions()) {
                    if (action.getId() == containerDTO.getId()) {
                        actions.add(action);
                    }
                }
            }
        }
        return actions;
    }

    private ArrayList<ComparisonContentDTO> compare(ArrayList<Action> beforeActions, ArrayList<Action> afterActions)
    {
        Action action1;
        Action action2;
        ArrayList<ComparisonContentDTO> comparisonContentDTOs = new ArrayList<>();

        for(int i = 0; i < beforeActions.size(); i++)
        {
            action1 = beforeActions.get(i);
            action2 = afterActions.get(i);

            System.out.println((String) action1.getCommandResult().getCommandResponse().getResult());
            System.out.println((String) action2.getCommandResult().getCommandResponse().getResult());

            comparisonContentDTOs.add(new ComparisonContentDTO(""+action1.getId(), action1.getCommandResult().getDisplayName(),
                    (String) action1.getCommandResult().getCommandResponse().getResult(),
                    (String) action2.getCommandResult().getCommandResponse().getResult()));
        }

        /*for(Connectable connectable : (ArrayList<Connectable>) array1)
        {
            for(Connectable connectable2 : (ArrayList<Connectable>) array2)
            {
                if(connectable.getHostName().equals(connectable2.getHostName()))
                {
                    for(Action action : connectable.getActions())
                    {

                    }

                    foundConnectable = true;
                    incompatible = false;
                    String divName = createDivName(connectable);

                    Accordion accordion = mainAccordion.addSubAccordion(new Accordion(connectable.getHostName(), divName, 0,null));

                    for(Command command : connectable.getActions())
                    {
                        for(Command command1 : connectable2.getActions())
                        {
                            if(command instanceof SendRemoteCommand && command1 instanceof SendRemoteCommand)
                            {
                                if(((SendRemoteCommand)command).getCommandString().equals(((SendRemoteCommand)command1).getCommandString()) &&
                                        command.isComparable() && command1.isComparable())
                                {
                                    foundCommand = true;

                                    String divName2 = createDivName(connectable);

                                    String before = (String)command.getCommandResponse().getResult();
                                    String after = (String)command1.getCommandResponse().getResult();

                                    //printDifference(before, after);

                                    accordion.addSubAccordion(new Accordion(((SendRemoteCommand) command).getCommandString(), divName2, lineDifference(before, after)));

                                    contentDtos.add(new ContentDto(before, after, divName2));
                                }
                            }
                        }
                    }
                    //Could not find any commands that match.
                    if(!foundCommand)
                    {
                        //contentDtos.add(new ContentDto("Could not find matching commands to compare in both Digital IDs", null, divName));
                    }
                }
            }
        }

        //Could find any connectable objects whose names match.
        if(!foundConnectable)
        {
            //contentDtos.add(new ContentDto("Could not find matching devices to compare in both Digital IDs", null, mainAccordion.getDivName()));
        }*/

        return comparisonContentDTOs;
    }

    private <T> void buildAccordions(ArrayList<T> array1, String title)
    {
        Accordion mainAccordion = new Accordion(title, title, 0,null);

        for(Connectable connectable : (ArrayList<Connectable>) array1)
        {
            System.out.println(connectable.getHostName());
                String divName = createDivName(connectable);

                Accordion accordion = mainAccordion.addSubAccordion(new Accordion(connectable.getHostName(), divName, 0, null));

                for(Action action : connectable.getActions())
                {
                    for(Command command : action.getCommands())
                    {
                        if(command.isComparable())
                        {
                            String resultDivName = createDivName(connectable);
                            accordion.addSubAccordion(new Accordion(command.getDisplayName(), resultDivName, 0, (String)command.getCommandResponse().getResult()));
                            contentDtos.add(new ContentDto(action.getId(), digitalID1.getName(), command.getDisplayName() + " on " + connectable.getHostName(), (String) command.getCommandResponse().getResult()));
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

        for(int i = 0; i < array1.length; i++)
        {
            array1[i] = array1[i].replaceAll("\\s+","");
        }

        for(int i = 0; i < array2.length; i++)
        {
            array2[i] = array2[i].replaceAll("\\s+","");
        }

        List<String> array3 = Arrays.asList(array1);
        List<String> array4 = Arrays.asList(array2);

        for(String line : array1)
        {
            if(!array4.contains(line))
            {
                difference += 1;
            }
        }

        return difference;
    }

    public void printDifference(String s0, String s1)
    {
        String[] no_white_spaces = s0.split("\\r?\\n");
        String[] no_white_spaces2 = s1.split("\\r?\\n");

        for(int i = 0; i < no_white_spaces.length; i++)
        {
            no_white_spaces[i] = no_white_spaces[i].replaceAll("\\s+","");
        }

        for(int i = 0; i < no_white_spaces2.length; i++)
        {
            no_white_spaces2[i] = no_white_spaces2[i].replaceAll("\\s+","");
        }

        List<String> array3 = Arrays.asList(no_white_spaces);
        List<String> array4 = Arrays.asList(no_white_spaces2);

        for(String line : no_white_spaces)
        {
            if(!array4.contains(line))
            {
                System.out.println("The difference is: "+line);
            }
        }

        for(String line : no_white_spaces2)
        {
            if(!array3.contains(line))
            {
                System.out.println("The difference is: "+line);
            }
        }


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
