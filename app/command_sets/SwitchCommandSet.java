package command_sets;

/**
 * Created by wasinski on 19/02/2015.
 */
public abstract class SwitchCommandSet extends CommandSet
{
    public abstract String GET_SYSTEM_VERSION();

    public abstract String GET_FLOG_DATABASE();

    public abstract String GET_PORT_INFORMATION(String portNumber);
}
