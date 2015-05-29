package dtos;

public class ContentDto
{
    private String displayString;
    private String content;
    private int id;

    public ContentDto(int id, String displayString, String content) {
        this.id = id;
        this.displayString = displayString;
        this.content = content;
    }
}
