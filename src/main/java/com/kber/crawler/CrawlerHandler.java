package com.kber.crawler;

import com.google.inject.Inject;
import com.kber.aop.Repeat;
import com.kber.crawler.model.Config;
import com.kber.crawler.model.Country;
import com.kber.crawler.proxy.ProxyHost;
import com.kber.crawler.proxy.ProxyManager;
import com.kber.crawler.utils.Constants;
import com.kber.crawler.utils.PageLoadHelper;
import com.kber.crawler.utils.Tools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.*;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/26/2016 10:20 AM
 */
@Singleton
public class CrawlerHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(CrawlerHandler.class);
    private Config config = Tools.loadCustomize(Constants.CONFIG_CUSTOMIZE);
    @Inject private CrawlerThreadPool crawlerThreadPool;
    @Inject private ProxyManager proxyManager;

    public List<String> getKeywords() {
        File keywordsFile = new File("keywords.txt");
        List<String> keywords = new ArrayList<>();
        BufferedInputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(keywordsFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        String line;
        int startLine = config.getStartLine();
        int i = 0;
        try {
            while ((line = reader.readLine()) != null) {
                if (line == null || i++ < startLine) {
                    continue;
                }
                keywords.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keywords;

    }

    public void execute() throws IOException {
        List<List<String>> lists = Tools.partitionQueries(config, getKeywords());
        final CountDownLatch countDownLatch = new CountDownLatch(lists.size());
        final AtomicInteger threadIndex = new AtomicInteger(0);
        long begin = System.currentTimeMillis();
        for (final List<String> keywords : lists) {
            crawlerThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        long beginning = System.currentTimeMillis();
                        File asinFile = new File("asin" + threadIndex + ".txt");
                        BufferedWriter bwr = new BufferedWriter(new FileWriter(asinFile));
                        threadIndex.getAndAdd(1);
                        LOGGER.info("开始第{}组抓取", threadIndex);
                        int i = 0;
                        for (String keyword : keywords) {
                            i++;
                            long beginning1 = System.currentTimeMillis();
                            final AtomicBoolean hasNext = new AtomicBoolean(true);
                            for (int n = 0; n < 2; ++n) {
                                try {
                                    if (hasNext.get()) {
                                        long beginning2 = System.currentTimeMillis();
                                        hasNext.set(retrieve(config.getCountry(), keyword, config.getCategory(), n, bwr));
                                        LOGGER.info("完成第{}组第{}个关键词第{}页抓取,耗时{}", threadIndex, i, n, Tools.formatCostTime(beginning2));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            LOGGER.info("完成第{}组第{}个关键词抓取,耗时{}", threadIndex, i, Tools.formatCostTime(beginning1));
                            PageLoadHelper.WaitTime.Short.execute();
                        }
                        LOGGER.info("完成第{}组抓取,耗时{}", threadIndex, Tools.formatCostTime(beginning));
                        countDownLatch.countDown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            Tools.await(countDownLatch);

        }
        LOGGER.info("完成抓取,耗时{}", threadIndex, Tools.formatCostTime(begin));
    }

    @Repeat(errorMessageBlackList = {"rateLimitExceeded", "User-rate limit exceeded"},
            expectedException = ConnectException.class, sleepTime = 12)
    public String getPage(String address) {
        URL url = null;
        String page = "";
        InputStream in = null;
        ProxyHost proxyHost = null;
        URLConnection uc = null;
        PageLoadHelper.WaitTime.Short.execute();
        try {
            url = new URL(address);
            if (config.isNeedProxy()) {
                proxyHost = proxyManager.getProxy();
                uc = ConnectionPool.getConnection(url, proxyHost);
            } else {
                uc = ConnectionPool.getConnection(url);
            }
            uc.connect();
            in = uc.getInputStream();
            String line = null;
            StringBuffer tmp = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                tmp.append(line);
            }
            page = tmp.toString();
        } catch (ConnectException e) {
            e.printStackTrace();
            proxyManager.removeProxy(proxyHost);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                    if (config.isNeedProxy()) {
                        proxyManager.addProxy(proxyHost);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return page;
    }

    public boolean retrieve(Country country, String keyword, String category, int page, BufferedWriter bwr) throws IOException {
        boolean hasNextPage = false;
        StringBuffer stringBuffer = new StringBuffer();
        String url = country.getBaseUrl() + "/s/?rh=n%3A" + category + "%2Ck%3A" + "&keywords=" + keyword + "&page=" + page;
        String body = getPage(url);
        if (body.length() > 0) {
            Document docRoot = Jsoup.parse(body);
            Element products = docRoot.getElementById("s-results-list-atf");
            for (Element e : products.select("li.s-result-item")) {
                stringBuffer.append(e.attr("data-asin"));
                stringBuffer.append("\n");
            }

            bwr.write(stringBuffer.toString());
            bwr.flush();

            Element nextPage = docRoot.getElementById("pagnNextString");
            if (nextPage != null) {
                hasNextPage = true;
            }
        }
        return hasNextPage;
    }
}
