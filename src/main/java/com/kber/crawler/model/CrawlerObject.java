package com.kber.crawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/21/2016 4:14 PM
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class CrawlerObject {
    private String baseUrl;
    private String keyword;
    private int page;

    public abstract String getUrl();
}
