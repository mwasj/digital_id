package models;

/**
 * Created by wasinski on 30/04/2015.
 */
public class WebUpdate<T>
{
    private String text;
    private long  id;
    private T content;
    private WebUpdateType webUpdateType;

    public T getContent() {
        return content;
    }

    public WebUpdateType getWebUpdateType() {
        return webUpdateType;
    }

    public String getText() {
        return text;
    }

    public long getId() {
        return id;
    }

    public WebUpdate(String text, long id, T content, WebUpdateType webUpdateType)
    {
        this.text = text;
        this.id = id;
        this.content = content;
        this.webUpdateType = webUpdateType;
    }


}
