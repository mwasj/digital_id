package dtos;

import java.util.ArrayList;

/**
 * Created by wasinski on 02/06/2015.
 */
public class ComparisonDTO
{
    private ArrayList<ContainerDTO> beforeArray;
    private ArrayList<ContainerDTO> afterArray;

    public ArrayList<ContainerDTO> getBeforeArray() {
        return beforeArray;
    }

    public ArrayList<ContainerDTO> getAfterArray() {
        return afterArray;
    }

    public ComparisonDTO(ArrayList<ContainerDTO> beforeArray, ArrayList<ContainerDTO> afterArray) {
        this.beforeArray = beforeArray;
        this.afterArray = afterArray;
    }
}
