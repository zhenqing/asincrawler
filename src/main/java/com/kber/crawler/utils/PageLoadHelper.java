package com.kber.crawler.utils;

import com.kber.crawler.model.Country;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/22/2016 5:06 PM
 */
public class PageLoadHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageLoadHelper.class);

    /**
     * 等待时间枚举
     *
     * @author <a href="mailto:nathanael4ever@gmail.com">Nathanael Yang</a> Feb 25, 2015
     */
    public enum WaitTime {
        /**
         * 超长: 1分钟
         */
        SuperLong(60),
        /**
         * 最长: 30秒
         */
        Longest(30),
        /**
         * 较长：20秒
         */
        Longer(20),
        /**
         * 长：10秒
         */
        Long(10),
        /**
         * 一般：5秒
         */
        Normal(5),
        /**
         * 短：3秒
         */
        Short(3),
        /**
         * 较短：2秒
         */
        Shorter(2),
        /**
         * 最短：1秒
         */
        Shortest(1);

        private final int value;

        public int val() {
            return value;
        }

        public int valInMS() {
            return value * 1000;
        }

        public void execute() {
            Tools.sleep(this.valInMS());
        }

        WaitTime(int value) {
            this.value = value;
        }
    }

    /**
     * 等待结果特征类型枚举
     *
     * @author <a href="mailto:nathanael4ever@gmail.com">Nathanael Yang</a> Feb 26, 2015
     */
    public enum Indicator {
        /**
         * 元素存在
         */
        Present,
        /**
         * 元素存在且可见
         */
        Visible,
        /**
         * 元素不可见(不存在等情况也等同于不可见)
         */
        Invisible,
        /**
         * 元素可点击
         */
        Clickable
    }

    /**
     * <pre>
     * 页面特征属性，主要用于判定页面跳转是否完成，判定逻辑如下:
     * 1. 在限定的等待时间里面，页面的标题、url是否发生变化?
     * 2. 在限定的等待时间里面，下一个跳转页面的标识元素是否已经出现?
     * 3. 如果以上两者均不满足，判定为页面未正常提交或提交时间超时
     * </pre>
     *
     * @author <a href="mailto:nathanael4ever@gmail.com">Nathanael Yang</a> Feb 26, 2015
     */
    public static class PageCharacteristic {
        public PageCharacteristic(String formerTitle, String formerUrl, By flagLocator, boolean pipeline, Country country) {
            this.formerTitle = formerTitle;
            this.formerUrl = formerUrl;
            this.flagLocator = flagLocator;
            this.pipeline = pipeline;
            this.country = country;
        }

        /**
         * 跳转之前页面标题
         */
        private String formerTitle;
        /**
         * 跳转之前页面Url地址
         */
        private String formerUrl;
        /**
         * 页面特征元素的Locator
         */
        private By flagLocator;
        /**
         * 是否为一屏式模式
         */
        private boolean pipeline;
        /**
         * 当前国家
         */
        private Country country;

        public String getFormerTitle() {
            return formerTitle;
        }

        public void setFormerTitle(String formerTitle) {
            this.formerTitle = formerTitle;
        }

        public String getFormerUrl() {
            return formerUrl;
        }

        public void setFormerUrl(String formerUrl) {
            this.formerUrl = formerUrl;
        }

        public By getFlagLocator() {
            return flagLocator;
        }

        public void setFlagLocator(By flagLocator) {
            this.flagLocator = flagLocator;
        }

        public boolean isPipeline() {
            return pipeline;
        }

        public void setPipeline(boolean pipeline) {
            this.pipeline = pipeline;
        }

        public Country getCountry() {
            return country;
        }

        public void setCountry(Country country) {
            this.country = country;
        }
    }

    public static boolean titleContains(WebDriver driver, String titlePattern, WaitTime waitTime) {
        try {
            return (new WebDriverWait(driver, waitTime.val())).until(titleContains(titlePattern));
        } catch (WebDriverException e) {
            return false;
        }
    }

    public static boolean titleNotContains(WebDriver driver, String titlePattern, WaitTime waitTime) {
        try {
            return (new WebDriverWait(driver, waitTime.val())).until(titleNotContains(titlePattern));
        } catch (WebDriverException e) {
            return false;
        }
    }

    public static boolean urlContains(WebDriver driver, String urlPattern, WaitTime waitTime) {
        try {
            return (new WebDriverWait(driver, waitTime.val())).until(urlContains(urlPattern));
        } catch (WebDriverException e) {
            return false;
        }
    }

    public static boolean urlNotContains(WebDriver driver, String urlPattern, WaitTime waitTime) {
        try {
            return (new WebDriverWait(driver, waitTime.val())).until(urlNotContains(urlPattern));
        } catch (WebDriverException e) {
            return false;
        }
    }

    /**
     * 期待网页标题包含(不区分大小写)指定的关键词
     *
     * @param title 期待的网页标题关键词
     * @see ExpectedConditions#titleContains(String)
     */
    public static ExpectedCondition<Boolean> titleContains(final String title) {
        return new ExpectedCondition<Boolean>() {
            private String currentTitle = null;

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    currentTitle = driver.getTitle();
                    return Tools.contains(currentTitle, title);
                } catch (WebDriverException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("Title pattern is '%s', current title is '%s'", title, currentTitle);
            }
        };
    }

    public static ExpectedCondition<Boolean> titleNotContains(final String title) {
        return new ExpectedCondition<Boolean>() {
            private String currentTitle = null;

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    currentTitle = driver.getTitle();
                    return !title.equals(currentTitle) && !Tools.contains(currentTitle, title);
                } catch (WebDriverException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("Title pattern is '%s', current title is '%s'", title, currentTitle);
            }
        };
    }

    public static boolean textContains(WebDriver driver, final String text, final By by) {
        try {
            return (new WebDriverWait(driver, WaitTime.Short.val())).until(textContains(text, by));
        } catch (WebDriverException e) {
            return false;
        }
    }

    /**
     * 期待某一网页元素的文本包含指定的关键内容
     *
     * @param text 待包含的文本内容
     * @param by   网页元素对应Locator
     */
    public static ExpectedCondition<Boolean> textContains(final String text, final By by) {
        return new ExpectedCondition<Boolean>() {
            private String currentText = null;

            @Override
            public Boolean apply(WebDriver driver) {
                currentText = driver.findElement(by).getText();
                return Tools.contains(currentText, text);
            }

            @Override
            public String toString() {
                return String.format("Text Pattern is '%s', current text is '%s'", text, currentText);
            }
        };
    }

    /**
     * 期待网页Url地址包含指定的关键内容
     *
     * @param url 期待的网页Url地址关键内容
     */
    public static ExpectedCondition<Boolean> urlContains(final String url) {
        return new ExpectedCondition<Boolean>() {
            private String currentUrl = null;

            @Override
            public Boolean apply(WebDriver driver) {
                currentUrl = driver.getCurrentUrl();
                return Tools.contains(currentUrl, url);
            }

            @Override
            public String toString() {
                return String.format("Url Pattern is '%s', current url is '%s'", url, currentUrl);
            }
        };
    }

    /**
     * 期待网页Url地址不包含指定的关键内容
     *
     * @param url 期待的网页Url地址关键内容
     */
    public static ExpectedCondition<Boolean> urlNotContains(final String url) {
        return new ExpectedCondition<Boolean>() {
            private String currentUrl = null;

            @Override
            public Boolean apply(WebDriver driver) {
                currentUrl = driver.getCurrentUrl();
                return !Tools.contains(currentUrl, url);
            }

            @Override
            public String toString() {
                return String.format("Url Pattern is '%s', current url is '%s'", url, currentUrl);
            }
        };
    }


    public static boolean present(WebDriver driver, By by, WaitTime waitTime) {
        return wait(driver, Indicator.Present, by, waitTime);
    }

    public static boolean visible(WebDriver driver, By by, WaitTime waitTime) {
        return wait(driver, Indicator.Visible, by, waitTime);
    }

    public static boolean inVisible(WebDriver driver, By by, WaitTime waitTime) {
        return wait(driver, Indicator.Invisible, by, waitTime);
    }

    public static boolean clickable(WebDriver driver, By by, WaitTime waitTime) {
        return wait(driver, Indicator.Clickable, by, waitTime);
    }

    /**
     * 以Explicit Wait的方式，根据Locator寻找对应的页面元素
     *
     * @param driver   当前WebDriver
     * @param by       页面元素Locator
     * @param waitTime 超时时间设定
     * @return 如果页面元素在给定时间内找到(没有可见约束)，返回该元素，反之返回<strong>null</strong>，调用方需做校验处理
     */
    public static WebElement findElement(WebDriver driver, By by, WaitTime waitTime) {
        return (new WebDriverWait(driver, waitTime.val())).until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * 在给定的超时时间范围内，等待、判定期待的结果是否出现
     *
     * @param driver    当前WebDriver
     * @param indicator 等待结果标识类型
     * @param by        页面元素Locator
     * @param waitTime  超时时间设定
     * @return 期待的结果是否出现
     */
    public static boolean wait(WebDriver driver, Indicator indicator, By by, WaitTime waitTime) {
        try {
            switch (indicator) {
                case Present:
                    return (new WebDriverWait(driver, waitTime.val())).until(ExpectedConditions.presenceOfElementLocated(by)) != null;
                case Visible:
                    return (new WebDriverWait(driver, waitTime.val())).until(ExpectedConditions.visibilityOfElementLocated(by)) != null;
                case Invisible:
                    return (new WebDriverWait(driver, waitTime.val())).until(ExpectedConditions.invisibilityOfElementLocated(by));
                case Clickable:
                    return (new WebDriverWait(driver, waitTime.val())).until(ExpectedConditions.elementToBeClickable(by)) != null;
            }

            throw new IllegalArgumentException("Unsupported Indicator: " + indicator);
        } catch (WebDriverException e) {
            return false;
        }
    }

    /**
     * 判定当前做单是否使用一屏式页面模式
     */
    public static boolean pipelined(WebDriver driver) {
        Document doc = Jsoup.parse(driver.getPageSource());
        return doc.select("#pipelinedPageTitle").size() > 0 &&
                doc.select("#shipaddress").size() > 0 &&
                doc.select("#giftoptions").size() > 0;
    }
}
