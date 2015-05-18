package connectionManager;

import core.ConnectionManager;
import models.Host;
import models.WindowsHost;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by wasinski on 15/05/2015.
 */
public class ConnectionManagerTest
{
    private Host windowsHost;
    private ConnectionManager connectionManager;

    @Before
    public void setUp()
    {
        windowsHost = new WindowsHost("dl380pg8-74", "Administrator", "ssmssm", null);
        connectionManager = new ConnectionManager(windowsHost);
    }

    @Test
    public void testEstablishConnectionWithUsernameAndPassword()
    {
        connectionManager.connect();
        Assert.assertEquals("Connection is successfully established", connectionManager.getSession().isConnected(), true);
    }

    @Test
    public void testDisconnectSession()
    {
        if(connectionManager.getSession() == null)
        {
            connectionManager.connect();
            Assert.assertEquals("Connection is successfully established", connectionManager.getSession().isConnected(), true);
        }

        connectionManager.getSession().disconnect();

        Assert.assertEquals("Connection should be terminated", connectionManager.getSession().isConnected(), false);
    }
}
