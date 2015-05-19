package gson;

import actions.Action;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import predefined_actions.Sg3Utils;

import java.util.ArrayList;

/**
 * Created by wasinski on 19/05/2015.
 */
public class GsonTest
{
    @Test
    public void testGsonAction()
    {
        System.out.println(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(Sg3Utils.windowsSg3UtilsAction()));
    }
}
