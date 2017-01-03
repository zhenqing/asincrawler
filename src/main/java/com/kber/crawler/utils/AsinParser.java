package com.kber.crawler.utils;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class AsinParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(AsinParser.class);

    private String joinASIN(Collection<String> asins) {
        if (CollectionUtils.isEmpty(asins)) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (String asin : asins) {
            if (index >= 100) {
                sb.append(Constants.ELLIPSIS);
                break;
            }
            sb.append(Constants.COMMA).append(asin);
            index++;
        }
        return sb.toString().substring(1);
    }

    List<String> retrieveASIN(String body) {
        String converted = body.replaceAll("\\b[A-Z|0-9]{3}-[A-Z|0-9]{10}\\b", StringUtils.EMPTY)
                // 去掉形如"&qid=1471276852&rnid=2528832011"的噪音
                .replaceAll("(&|#|[?])[a-z|A-Z|_]+=[0-9]{10}", StringUtils.EMPTY).replaceAll("(Account#:|CASE|Case ID|issue:|case #) [0-9]{10}", StringUtils.EMPTY)
                // 去掉链接地址中形如%3A11058281%这样的内容
                .replaceAll("%[A-Z|0-9]{10}%", StringUtils.EMPTY)
                // 去掉形如"Inquiry from Amazon customer 3275254907", "3275254907 - Amazon Marketplace <wpr8hj096tpnty8@marketplace.amazon.com>"的噪音
                .replaceAll("from Amazon customer [0-9]{10}", StringUtils.EMPTY).replaceAll("[0-9]{10} - Amazon Marketplace", StringUtils.EMPTY);
        List<String> list = RegexUtils.getMatchedList(converted, RegexUtils.Regex.ASIN);
        this.filterInvalidASIN(list);
        list = ImmutableSet.copyOf(list).asList();
        return list;
    }


    private void filterInvalidASIN(List<String> list) {
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            String s = iterator.next();
            if (!Tools.isASIN(s)) {
                LOGGER.debug("{}不是有效的ASIN, 略过", s);
                iterator.remove();
            }
        }
    }
}
