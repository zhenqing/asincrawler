package com.kber.crawler.model;

import com.kber.commons.model.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 1/2/2017 3:27 PM
 */
@Table("crawl_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CrawlLog extends PrimaryKey{
    @Name private String keyword;
    @Column private Date lastCrawlTime;

    @Override
    public String getPK() {
        return keyword;
    }
}
