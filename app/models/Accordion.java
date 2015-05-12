package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wasinski on 10/02/2015.
 */
public class Accordion
{
    private String title;
    private ArrayList<Accordion> subAccordions;
    private Accordion parentAccordion;
    private String divName;
    private int noOfChanges;
    private Map<String, Integer> map;
    private String context;

    public String getDivName() {
        return divName;
    }

    public void setParentAccordion(Accordion parentAccordion) {
        this.parentAccordion = parentAccordion;
    }

    public ArrayList<Accordion> getSubAccordions() {
        return subAccordions;
    }

    public void setSubAccordions(ArrayList<Accordion> subAccordions) {
        this.subAccordions = subAccordions;
    }

    public String getTitle() {
        return title;
    }

    public int getNoOfChanges()
    {
        return noOfChanges;
    }

    public Accordion getParentAccordion() {
        return parentAccordion;
    }

    public Accordion(String title, String divName, int noOfChanges)
    {
        this.divName = divName;
        this.title = title;
        this.subAccordions = null;
        this.noOfChanges = noOfChanges;
        this.map = new HashMap<>();
    }

    public void passChangesToParent(int a, String source)
    {
        if(!map.containsKey(source))
        {
            map.put(source, a);
            noOfChanges += a;
        }

        if(getParentAccordion() != null)
        {
            getParentAccordion().passChangesToParent(a, source);
        }
    }

    /**
     * Adds a sub accordion to the subaccordions list.
     * @param accordion
     * @return
     */
    public Accordion addSubAccordion(Accordion accordion)
    {
        if(subAccordions == null)
        {
            subAccordions = new ArrayList<>();
        }

        accordion.setParentAccordion(this);
        subAccordions.add(accordion);

        map.put(accordion.getTitle(), accordion.getNoOfChanges());
        noOfChanges += accordion.getNoOfChanges();

        if(getParentAccordion() != null)
        {
            getParentAccordion().passChangesToParent(accordion.getNoOfChanges(), accordion.getDivName());
        }

        return accordion;
    }

}
