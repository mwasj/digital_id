package dtos;

/**
 * Created by wasinski on 02/06/2015.
 */
public class ComparisonContentDTO
{
    private String divName;
    private String displayString;
    private String beforeContent;
    private String afterContent;

    public ComparisonContentDTO(String divName, String displayString, String beforeContent, String afterContent) {
        this.divName = divName;
        this.displayString = displayString;
        this.beforeContent = beforeContent;
        this.afterContent = afterContent;
    }
}
