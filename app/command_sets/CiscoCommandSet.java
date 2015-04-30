package command_sets;

/**
 * Created by wasinski on 19/02/2015.
 */
public class CiscoCommandSet extends SwitchCommandSet
{
    @Override
    public String GET_SYSTEM_VERSION() {
        return "show version";
    }

    @Override
    public String GET_FLOG_DATABASE()  {
        return "show flogi database";
    }

    @Override
    public String GET_PORT_INFORMATION(String portNumber) { return "show interface " + portNumber; }
}
