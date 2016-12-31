package com.kber.crawler;

import com.google.inject.Singleton;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 11/18/2016 10:15 AM
 */
@Singleton
public class CrawlerThreadPool {
    private final ExecutorService executor;

    public CrawlerThreadPool() {
        this.executor = Executors.newFixedThreadPool(10, new BasicThreadFactory.Builder().namingPattern("Crawler-%d").daemon(true).build());
    }

    public void submit(Runnable task) {
        this.executor.submit(task);
    }
}
