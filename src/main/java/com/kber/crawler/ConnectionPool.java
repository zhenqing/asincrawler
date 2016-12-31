package com.kber.crawler;

import com.google.inject.Inject;
import com.kber.crawler.proxy.ProxyHost;
import com.kber.crawler.proxy.ProxyManager;

import java.io.IOException;
import java.net.*;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 11/18/2016 11:45 AM
 */
public class ConnectionPool {
@Inject ProxyManager proxyManager;

    public static URLConnection getConnection(URL url, ProxyHost proxyHost) {
        SocketAddress addr = new InetSocketAddress(proxyHost.getAddress(), proxyHost.getPort());
        Proxy typeProxy = new Proxy(Proxy.Type.HTTP, addr);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection(typeProxy);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conn;

    }

    public static URLConnection getConnection(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "MSIE");
            conn.setConnectTimeout(1000 * 5);
            conn.setReadTimeout(1000 * 30);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conn;
    }



}
