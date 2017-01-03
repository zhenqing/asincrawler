package com.kber.crawler;

import com.google.inject.Inject;
import com.kber.aop.Repeat;
import com.kber.crawler.db.AbstractDBManager;
import com.kber.crawler.db.DBUtils;
import com.kber.crawler.model.Config;
import com.kber.crawler.model.Country;
import com.kber.crawler.model.CrawlLog;
import com.kber.crawler.model.KeywordManager;
import com.kber.crawler.proxy.ProxyHost;
import com.kber.crawler.proxy.ProxyManager;
import com.kber.crawler.utils.Constants;
import com.kber.crawler.utils.PageLoadHelper;
import com.kber.crawler.utils.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.impl.FileSqlManager;
import org.nutz.dao.impl.NutDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.io.*;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/26/2016 10:20 AM
 */
@Singleton
public class CrawlerHandler extends AbstractDBManager {
    private final Logger LOGGER = LoggerFactory.getLogger(CrawlerHandler.class);
    @Inject private CrawlerThreadPool crawlerThreadPool;
    @Inject private ProxyManager proxyManager;
    @Inject private KeywordManager keywordManager = new KeywordManager();

    @Inject
    public CrawlerHandler() {
    }

    private void initDBDirectory(String absolutePath) {
        File dir = new File(absolutePath);
        if (!dir.exists() || !dir.isDirectory()) {
            if (!dir.mkdirs()) {
                throw new IllegalStateException(String.format("Failed to create sqlite database file storage directory %s.", dir.getAbsolutePath()));
            }
        }
    }

    @Inject
    @Override
    public void initDataSource() throws Exception {
        String absolutePath = "db";
        this.initDBDirectory(absolutePath);
        String url = String.format("jdbc:sqlite:%s/%s.db", absolutePath.replace(Constants.BACKSLASH, Constants.SLASH), this.getDBName());
        dao = new NutDao(DBUtils.buildSqliteDataSource(url), new FileSqlManager("db/tables.sql"));
        DBUtils.createTables(dao, "crawl_log", CrawlLog.class.getAnnotation(Table.class).value());
    }


    public String getDBName() {
        return "crawl_log";
    }

    public void execute(Config config) throws IOException {
        List<List<String>> lists = Tools.partitionQueries(config, keywordManager.getKeywords());
        CountDownLatch countDownLatch = new CountDownLatch(lists.size());
        int threadIndex = 1;
        long begin = System.currentTimeMillis();
        for (List<String> list : lists) {
            int threadNumber = threadIndex++;
            crawlerThreadPool.submit(() -> {
                try {
                    long beginning = System.currentTimeMillis();
                    File asinFile = new File("asin" + threadNumber + ".txt");
                    BufferedWriter bwr = new BufferedWriter(new FileWriter(asinFile));
                    LOGGER.info("开始第{}组抓取", threadNumber);
                    int i = 0;
                    AtomicBoolean hasNext;

                    for (final String keyword : list) {
                        CrawlLog log = this.readById(keyword, CrawlLog.class);
                        if (log != null) {
                            LOGGER.info("关键词{}已经在{}抓取过了", keyword, log.getLastCrawlTime());
                            continue;
                        }

                        long beginning1 = System.currentTimeMillis();
                        hasNext = new AtomicBoolean(true);
                        i = i + 1;
                        for (int n = 1; n < 401; n++) {
                            try {
                                if (hasNext.get()) {
                                    long beginning2 = System.currentTimeMillis();
                                    hasNext.set(retrieve(config.getCountry(), keyword, config.getCategory(), n, bwr, config));
                                    LOGGER.info("完成第{}组第{}个关键词{}第{}页抓取,耗时{}", threadNumber, i, keyword, n, Tools.formatCostTime(beginning2));
                                } else {
                                    break;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        log = new CrawlLog(keyword, new Date());
                        this.save(log, CrawlLog.class);
                        LOGGER.info("完成第{}组第{}个关键词抓取,耗时{}", threadNumber, i, Tools.formatCostTime(beginning1));
                    }
                    LOGGER.info("完成第{}组抓取,耗时{}", threadNumber, Tools.formatCostTime(beginning));
                    countDownLatch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        Tools.await(countDownLatch);
        LOGGER.info("完成抓取,耗时{}", threadIndex, Tools.formatCostTime(begin));
    }

    @Repeat(errorMessageBlackList = {"rateLimitExceeded", "User-rate limit exceeded"},
            expectedException = ConnectException.class, sleepTime = 12)
    public String getPage(String address, Config config) {
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
                LOGGER.info("use proxy:{}:{}", proxyHost.getAddress(), proxyHost.getPort());
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
        } catch (Exception e) {
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

    public boolean retrieve(Country country, String keyword, String category, int page, BufferedWriter bwr, Config config) throws IOException {
        boolean hasNextPage = false;
        StringBuffer stringBuffer = new StringBuffer();
        String url = country.getBaseUrl() + "/s/?rh=n%3A" + category + "%2Ck%3A" + "&keywords=" + keyword + "&page=" + page;
        String body = getPage(url, config);
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
