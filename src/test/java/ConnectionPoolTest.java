
import com.kber.crawler.ConnectionPool;
import com.kber.crawler.proxy.ProxyHost;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/19/2016 5:39 PM
 */
public class ConnectionPoolTest {
    @Test
    public void getConnection() throws MalformedURLException {
        URLConnection conn = ConnectionPool.getConnection(new URL("http://wwww.google.com"));
        Assert.assertEquals(conn.getURL(), new URL("http://wwww.google.com"));
    }

    @Test
    public void getConnectionWithProxy() throws MalformedURLException {
        ProxyHost proxyHost = new ProxyHost("201.55.46.6", 80);
        URLConnection conn = ConnectionPool.getConnection(new URL("http://wwww.google.com"), proxyHost);
        Assert.assertEquals(conn.getURL(), new URL("http://wwww.google.com"));
    }

}