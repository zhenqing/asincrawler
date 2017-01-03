package com.kber.crawler.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.google.common.base.Preconditions;
import com.kber.crawler.model.Config;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.nutz.lang.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class Tools {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tools.class);

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // -> Ignore
        }
    }

    public static void sleep(long time, TimeUnit timeunit) {
        try {
            timeunit.sleep(time);
        } catch (InterruptedException e) {
            // -> Ignore
        }
    }

    public static <T> List<List<T>> partitionQueries(Config config, @NotNull List<T> list) {
        if (config.isMultiThread()) {
            int threadCount = config.getThreadCount();
            List<List<T>> lists = ThreadHelper.assign(list, threadCount);
            LOGGER.info("{}个关键词分割为{}组并行处理", list.size(), lists.size());
            return lists;
        } else {
            return Collections.singletonList(list);
        }
    }

    public static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            // -> Ignore
        }
    }

    /**
     * 格式化从某一时间点开始到现在为止的消耗时间
     *
     * @param start 开始时间点的时间戳
     * @return 格式化好的消耗时间
     */
    public static String formatCostTime(long start) {
        long cost = System.currentTimeMillis() - start;
        return formatTime(cost);
    }

    /**
     * 格式化消耗时间
     *
     * @param cost 消耗时间
     * @return 格式化好的消耗时间
     */
    public static String formatTime(long cost) {
        for (int i = 0; i < TIME_RANGES.length; i++) {
            if (cost >= TIME_RANGES[i]) {
                double result = (double) cost / TIME_RANGES[i];
                return Constants.DOUBLE_FORMAT.format(result) + " s";
            }
        }

        return cost + " ms";
    }

    public static <T> T loadCustomize(Customize customize) {
        return loadCustomize(customize, customize.file());
    }

    public static <T> void saveCustomize(Customize customize, T t) {
        String json = JSON.toJSONString(t, true);
        try {
            FileUtils.writeStringToFile(customize.file(), json, Constants.UTF8);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadCustomize(Customize customize, File file) {
        long start = System.currentTimeMillis();

        String content = StringUtils.EMPTY;
        Object result = null;
        try {
            content = FileUtils.readFileToString(file, Constants.UTF8);

            result = customize.listMode() ? JSON.parseArray(content, customize.clazz()) : JSON.parseObject(content, customize.clazz());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            LOGGER.warn("{}对应配置文件不符合JSON规范:", customize.fileName(), e);
        }

        return (T) result;
    }

    public static <K, V> Map<K, V> map(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> map(K[] keys, V[] values) {
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    /**
     * 判定一个字符串是否包含给定的标识字符，不区分大小写
     *
     * @param source  源字符串
     * @param strings 目标字符组，必须每个都包含才算有效
     */
    public static boolean contains(String source, String... strings) {
        if (StringUtils.isBlank(source)) {
            return false;
        }
        for (String s : strings) {
            if (!source.toUpperCase().contains(s.toUpperCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判定一个字符串是否包含给定标识字符中的<strong>任意一个</strong>，不区分大小写
     *
     * @param source  源字符串
     * @param strings 目标字符组，包含<strong>任意一个</strong>即算有效
     */
    public static boolean containsAny(String source, String... strings) {
        if (StringUtils.isBlank(source)) {
            return false;
        }
        for (String s : strings) {
            if (source.toUpperCase().contains(s.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判定一个字符串是否等同(不区分大小写)于给定标识字符中的<strong>任意一个</strong>，不区分大小写
     *
     * @param source  源字符串
     * @param targets 目标字符组，等同(不区分大小写)<strong>任意一个</strong>即算有效
     */
    public static boolean equalsIgnoreCaseAny(String source, String... targets) {
        if (StringUtils.isBlank(source)) {
            return false;
        }
        for (String target : targets) {
            if (source.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判定给定的若干个字符串中是否至少有一个不为空
     *
     * @param texts 字符串数组
     */
    public static boolean isNotBlankAny(String... texts) {
        for (String s : texts) {
            if (StringUtils.isNotBlank(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判定给定的若干个字符串全不为空
     *
     * @param texts 字符串数组
     */
    public static boolean isNotBlank(String... texts) {
        for (String s : texts) {
            if (StringUtils.isBlank(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBlank(String... texts) {
        for (String s : texts) {
            if (StringUtils.isNotBlank(s)) {
                return false;
            }
        }
        return true;
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return map != null && !map.isEmpty();
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Determine whether a source string start with <strong>ANY</strong> one of targets
     * This process is in-case-sensitive
     */
    public static boolean startWithAny(String source, String... strings) {
        String _source = StringUtils.defaultString(source);
        for (String s : strings) {
            if (_source.toUpperCase().startsWith(s.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether a source string end with <strong>ANY</strong> one of targets
     * This process is in-case-sensitive
     */
    public static boolean endWithAny(String source, String... strings) {
        String _source = StringUtils.defaultString(source);
        for (String s : strings) {
            if (_source.toUpperCase().endsWith(s.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从形如(2 Items)或(2)等字符串中提取其中的数字
     *
     * @param source 源字符串
     */
    public static String parseNumber(String source) {
        int i1 = source.indexOf(Constants.OpenningBracket);
        int i2 = source.indexOf(Constants.ClosingBracket);
        int begin = i1 != -1 ? i1 + 1 : 0;
        int end = i2 != -1 ? i2 : source.length();
        return source.substring(begin, end).replaceAll(RegexUtils.Regex.NON_DIGITS.val(), StringUtils.EMPTY).trim();
    }

    /**
     * 将源字符串中非数字以外的内容全部去掉，解析为数字返回
     *
     * @param source 源字符串
     */
    public static int toInt(String source) {
        return NumberUtils.toInt(StringUtils.defaultString(source).replaceAll(RegexUtils.Regex.NON_DIGITS.val(), StringUtils.EMPTY).trim());
    }

    public static String getFirstLine(String src) {
        if (StringUtils.isBlank(src)) {
            return StringUtils.EMPTY;
        }

        String[] array = StringUtils.split(src, StringUtils.LF);
        return array[0].trim();
    }

    private static final String[] TIME_UNITS = {"h", "m", "s", "ms"};
    private static final long[] TIME_RANGES = {1000 * 60 * 60, 1000 * 60, 1000};


    private static final String INVALID_SOURCE_ISBN = "0000000000";

    public static boolean isASIN(String source) {
        return StringUtils.isNotBlank(source) && !INVALID_SOURCE_ISBN.equals(source) &&
                RegexUtils.Regex.ASIN.isMatched(source) && StringUtils.isNotBlank(source.replaceAll(RegexUtils.Regex.NON_DIGITS.val(), StringUtils.EMPTY));
    }

    public static String getBaseUrl(String url) {
        Preconditions.checkArgument(StringUtils.isNotBlank(url), "Source URL cannot be empty or null.");
        try {
            URL u = new URL(url);
            return u.getProtocol() + "://" + u.getHost();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
