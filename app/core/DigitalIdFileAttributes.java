package core;

public class DigitalIdFileAttributes
{
    private String fileName;
    private String creationTime;
    private String size;

    public DigitalIdFileAttributes(String fileName, String creationTime, String size) {
        this.fileName = fileName;
        this.creationTime = creationTime;
        this.size = size;
    }
}
