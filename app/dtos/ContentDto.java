package dtos;

public class ContentDto
{
    private String fileName;
    private String displayString;
    private String content;
    private int id;

    public ContentDto(int id, String fileName, String displayString, String content) {
        this.id = id;
        this.fileName = fileName;
        this.displayString = displayString;
        this.content = content;
    }
}
