package com.kber.crawler.proxy;

import com.google.inject.Inject;
import com.kber.crawler.service.ApplicationContext;
import lombok.Data;

import javax.inject.Singleton;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 11/18/2016 11:23 AM
 */
@Singleton
@Data
public class ProxyManager {
    private List<ProxyHost> proxyList;
    private List<ProxyHost> availableProxyList;

    ProxyManager() {
        proxyList = new ArrayList<>();
        init();
    }

    void init() {
        loadProxyList();
        availableProxyList = proxyList;
    }

    public void loadProxyList() {
        File proxyFile = new File("proxy.txt");
        BufferedInputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(proxyFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line == null) {
                    continue;
                }
                String[] s = line.split(":");
                if (s.length != 2) continue;
                try {
                    ProxyHost proxy = new ProxyHost(s[0], Integer.parseInt(s[1]));
                    proxyList.add(proxy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(proxyList.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ProxyHost> get_all_proxy() {
        if (proxyList.size() < 1) {
            loadProxyList();
        }
        return proxyList;
    }

    public ProxyHost getProxy() {
        int size = availableProxyList.size();
        int random = (int) (Math.random() * size + 1);
        ProxyHost proxyHost = availableProxyList.get(random);
        availableProxyList.remove(random);
        return proxyHost;
    }

    public void addProxy(ProxyHost proxyHost) {
        availableProxyList.add(proxyHost);
    }

    public void removeProxy(ProxyHost proxyHost) {
        proxyList.remove(proxyHost);
        availableProxyList.remove(proxyHost);
    }

    public static void main(String[] args) {
        List<ProxyHost> proxyList = new ProxyManager().getAvailableProxyList();
        System.out.println(proxyList.size());
    }


}
