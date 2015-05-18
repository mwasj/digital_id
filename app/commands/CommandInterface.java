package commands;

import core.CommandResponse;
import core.ConnectionManager;

/**
 * Created by wasinski on 15/05/2015.
 */
public interface CommandInterface
{
    public void initialise(ConnectionManager connectionManager);
    public CommandResponse execute();
}
