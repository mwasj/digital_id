package command_sets;

/**
 * Created by wasinski on 19/02/2015.
 */
public class BrocadeCommandSet extends SwitchCommandSet
{
    @Override
    public String GET_SYSTEM_VERSION() {
        return "switchshow";
    }

    @Override
    public String GET_FLOG_DATABASE() { return "portflagsshow | grep FLOGI"; }

    @Override
    public String GET_PORT_INFORMATION(String portNumber) { return "portshow " + portNumber; }
}
