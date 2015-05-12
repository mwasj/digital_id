package core;

import com.google.gson.Gson;
import dtos.ContentDto;
import dtos.DiffReportDto;
import models.Accordion;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

/**
 * Utility class responsible for generation of the diff report skeleton consisting of individual accordions.
 */
public class HtmlGenerator
{
    private Accordion accordion;
    private ArrayList<ContentDto> contentDtos;

    public HtmlGenerator(ArrayList<Accordion> accordions, ArrayList<ContentDto> contentDtos)
    {
        accordion = new Accordion(null, null, 0);
        this.contentDtos = contentDtos;
        accordion.setSubAccordions(accordions);
    }

    public String generateReportBackbone() throws IOException
    {
        return new Gson().toJson(new DiffReportDto(contentDtos, generateAccordions(accordion, "")));
    }

    private String generateAccordions(Accordion a,  String s)
    {
        if(a.getTitle() != null)
        {
            String style = (a.getNoOfChanges() == 0 ? "class=\"no_changes_detected\"" : "class=\"changes_detected\"");
            style = a.getNoOfChanges() == -1 ? "class=\"incompatible\"" : style;

            String changes = a.getNoOfChanges() == -1 ? "" : a.getNoOfChanges() +" changes detected";

            System.out.println(a.getTitle());
            s = s + "<accordion><accordion-group><accordion-heading><a "+ style + "> "
                    + a.getTitle() + " " + changes +"  </a> </accordion-heading><div dynamic="+a.getDivName()+"></div>";
        }

        if(a.getSubAccordions() != null)
        {
            for(Accordion accordion1 : a.getSubAccordions())
            {
                s = s + generateAccordions(accordion1, "");
                //s = s + "</accordion>";
            }
        }

        if(a.getTitle() != null)
        {
            s = s + "</accordion-group></accordion>";
        }

        return s;
    }

}

