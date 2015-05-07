package dtos;

import models.Accordion;

import java.util.ArrayList;

/**
 * Created by Michal on 07/05/2015.
 */
public class DiffReportDto
{
    private ArrayList<ContentDto> accordions;
    private String accordionHtml;

    public DiffReportDto(ArrayList<ContentDto> accordions, String accordionHtml) {
        this.accordions = accordions;
        this.accordionHtml = accordionHtml;
    }
}

