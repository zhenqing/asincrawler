package com.kber.crawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.inject.Singleton;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/21/2016 4:47 PM
 */
@Singleton
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Config {
    private Country country;
    private String category;
    private boolean needProxy;
    private boolean multiThread;
    private int threadCount;
}
