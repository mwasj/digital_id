package commands;

/**
 * Created by wasinski on 19/05/2015.
 */
public interface CommandUpdateInterface
{
    void sendCommandUpdate(CommandStatus commandStatus, String data, String webId);
}
