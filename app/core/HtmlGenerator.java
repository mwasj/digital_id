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
 * Created by Michal on 08/02/15.
 */
public class HtmlGenerator
{
    private Accordion accordion;
    private ArrayList<ContentDto> contentDtos;

    public HtmlGenerator(ArrayList<Accordion> accordions, ArrayList<ContentDto> contentDtos)
    {
        accordion = new Accordion(null, null, null, null, 0);
        this.contentDtos = contentDtos;
        accordion.setSubAccordions(accordions);
    }

    public  void generateHtml(String path) throws IOException
    {
        String htmlTemplate = new String(readAllBytes(get("C:\\digital_ids\\html\\template.html")));
        htmlTemplate = htmlTemplate.replaceAll("<#generate_html#>", generateDivs(accordion, ""));
        htmlTemplate = htmlTemplate.replaceAll("<#generate_function_calls#>", generateJsFunctionCalls(accordion, ""));
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(htmlTemplate);
        bw.close();
        Desktop.getDesktop().browse(new File(path).toURI());
    }

    public String getHtml(String path) throws IOException {
        /*String htmlTemplate = new String(readAllBytes(get("C:\\digital_ids\\html\\template2.html")));
        htmlTemplate = htmlTemplate.replaceAll("<#generate_html#>", generateDivs(accordion, ""));
        htmlTemplate = htmlTemplate.replaceAll("<#generate_function_calls#>", generateJsFunctionCalls(accordion, ""));
        return htmlTemplate;*/

        return new Gson().toJson(new DiffReportDto(contentDtos, generateAccordions(accordion, "")));
    }

    private String convertToJavaScriptString(String s)
    {
        String[] split = s.split("\\r?\\n");
        String javascriptString = "";

        for(String s1 : split)
        {
            javascriptString = javascriptString +  "'" + s1 + "\\\\" + "n ' + \n";
        }

        return javascriptString.substring(0, javascriptString.lastIndexOf("+"));
    }

    private String generateJsFunctionCalls(Accordion a, String s)
    {

        if(a.getBaseContent() != null && a.getContentToCompare() != null)
        {
            s = s + "diffUsingJS(0,'" + a.getDivName() + "'," + convertToJavaScriptString(a.getBaseContent()) + "," + convertToJavaScriptString(a.getContentToCompare()) +");";
        }

        if(a.getSubAccordions() != null)
        {
            for(Accordion accordion1 : a.getSubAccordions())
            {
                s = s + generateJsFunctionCalls(accordion1, "");
            }

        }

        return s;
    }

    private String generateDivs(Accordion a,  String s)
    {
        if(a.getTitle() != null)
        {
            s = s + "<div class=\"accordion\"><h3 " + (a.getNoOfChanges() == 0 ? "class=\"no_changes_detected\"" : "class=\"changes_detected\"") + ">" + a.getTitle() + " " + a.getNoOfChanges() + " changes detected</h3><div id=\"" + a.getDivName() + "\">";
        }

        if(a.getSubAccordions() != null)
        {
            for(Accordion accordion1 : a.getSubAccordions())
            {
                s = s + generateDivs(accordion1, "");
                s = s + "</div>";
            }
        }

        if(a.getTitle() != null)
        {
            s = s + "</div>";
        }

        return s;
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

