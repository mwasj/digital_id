package core;

public class DigitalIdFileAttributes
{
    private String displayName;
    private String url;
    private String creationTime;
    private String size;
    private String author;

    public DigitalIdFileAttributes(String author, String url, String displayName, String creationTime, String size)
    {
        this.author = author;
        this.displayName = displayName;
        this.url = url;
        this.creationTime = creationTime;
        this.size = size;
    }
}
