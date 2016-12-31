package com.kber.crawler.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/20/2016 5:54 PM
 */
public class PrettyFormatter {

    /**
     * 去除源字符串中HTML Tag以获取纯文本部分内容, 需要注意的是这一方法并不能作为一个通用、健壮的解决方案, 仅限于处理发给客人的灰条例信邮件
     *
     * @param source 源字符串, 通常为发给客人的灰条例信内容
     */
    public static String removeHtmlTags(String source) {
        // <br>, <BR>, <br />等等需要替换为换行符, 较为特殊的是形如<br style="font-size:12.727272033691406px">
        return source.replaceAll("<(br|BR) ?/?([^<>]+)?>", StringUtils.LF)
                .replaceAll("(&nbsp;|<([^>]+)>)", StringUtils.EMPTY)
                .replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "&").replaceAll("&#38;", "&")
                .replaceAll("&#39;", "'")
                .replaceAll("&gt;", ">")
                .replaceAll("&lt;", "<").trim();
    }
}
