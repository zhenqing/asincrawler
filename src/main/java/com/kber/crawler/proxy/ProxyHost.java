package com.kber.crawler.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 11/15/2016 4:21 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProxyHost {
    private String address;
    private int port;
}
