package dtos;

/**
 * Created by wasinski on 02/06/2015.
 */
public class ContainerDTO
{
    private int id;
    private String fileName;

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public ContainerDTO(int id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }
}
