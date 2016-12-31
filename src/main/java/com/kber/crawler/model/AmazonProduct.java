package com.kber.crawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/21/2016 4:46 PM
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AmazonProduct extends CrawlerObject {
    private Country country;
    private String category;
    private String keyword;

    @Override
    public String getUrl() {
        return getCountry().getBaseUrl() + "/s/?me=" + getCategory().trim() + "&page=" + getPage() + "&keywords=" + getKeyword();
    }
}
