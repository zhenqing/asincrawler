package com.kber.crawler.utils;

import com.kber.crawler.model.Config;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/20/2016 5:56 PM
 */
public class Constants {

    public static final Customize CONFIG_CUSTOMIZE = new Customize("config.js", Config.class, false);

    public static final String HTTP = "http";

    public static final String HTTPS = "https";

    public static final String LOCALHOST = "localhost";

    /**
     * 亚马逊各国网站首页地址前缀: {@value}
     */
    public static final String AMAZON_HOST_PREFIX = HTTP + "://www.amazon.";

    /**
     * 亚马逊各国Seller后台首页地址前缀 : {@value}
     */
    public static final String AMAZON_SELLER_HOST_PREFIX = HTTPS + "://sellercentral.amazon.";

    /**
     * 当前程序运行根目录
     */
    public static String ROOT_DIR = SystemUtils.USER_DIR;

    public static final DecimalFormat SINGLE_FORMAT = new DecimalFormat("0.0");
    public static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("0.00");
    public static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("0.000");
    /**
     * 电话号码：如果没有有效号码，直接填写:{@value}
     */
    public static final String DUMMY_PHONE_NUMBER = "123456";

    public static final String DUMMY = "#";

    /**
     * 兼容Windows记事本换行格式的换行符，方便日志查看
     */
    public static final String NEW_LINE = "\r\n";

    /**
     * 制表符: {@value}
     */
    public static final String TAB = "\t";

    /**
     * 默认字符集：UTF-8
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 产品转运标识，程序通过标题区分是否为产品转运:{@value}
     */
    public static final String PRODUCT_LABEL = "product";

    /**
     * Linux系统下中文文本字体:{@value}
     */
    public static final String LINUX_TEXT_FONT = "wqy-microhei";

    /**
     * 纯文本内容显示字体大小:{@value}
     */
    public static final int TEXT_FONT_SIZE = 13;

    public static final Font TEXT_FONT = new Font("Microsoft Yahei", Font.PLAIN, Constants.TEXT_FONT_SIZE);

    /**
     * 对数字类型文本做容错处理时，需补上缺少的字符内容:{@value}
     */
    public static final char DIGIT_ZERO = '0';

    /**
     * 逗号，可以用作CSV分隔符等 : {@value}
     */
    public static final String COMMA = ",";

    /**
     * 逗号 + 空格，用作字符串拼接连接符
     */
    public static final String COMMA_WHITESPACE = ", ";

    /**
     * 句号、点号，用作语句结束、货币分位等: {@value}
     */
    public static final String FULL_STOP = ".";

    /**
     * 冒号: {@value}
     */
    public static final String COLON = ":";

    /**
     * 减号、连接号: {@value}
     */
    public static final String HYPHEN = "-";

    /**
     * 下划线: {@value}
     */
    public static final String UNDERSCORE = "_";

    /**
     * 右括号: {@value}
     */
    public static final String ClosingBracket = ")";

    /**
     * 左括号: {@value}
     */
    public static final String OpenningBracket = "(";

    /**
     * 左花括号: {@value}
     */
    public static final String OpenningCurlyBracket = "{";

    /**
     * 右花括号: {@value}
     */
    public static final String ClosingCurlyBracket = "}";

    /**
     * 左尖括号: {@value}
     */
    public static final String OpenningAngleBrackets = "<";

    /**
     * 右尖括号: {@value}
     */
    public static final String ClosingAngleBrackets = ">";

    /**
     * 等于号: {@value}
     */
    public static final String EQUALS = "=";

    /**
     * 双引号: {@value}
     */
    public static final String DOUBLE_QUOTE = "\"";

    /**
     * 省略号: {@value}
     */
    public static final String ELLIPSIS = "...";

    /**
     * 斜线号: {@value}
     */
    public static final String SLASH = "/";

    /**
     * 反斜线号: {@value}
     */
    public static final String BACKSLASH = "\\";

    /**
     * 竖线: {@value}
     */
    public static final String VERTICAL_BAR = "|";

    /**
     * and连接符: {@value}
     */
    public static final String AND = "&";

    /**
     * 问号: {@value}
     */
    public static final String QUESTION_MARK = "?";

    public static final String AT = "@";

    public static final String STAR = "*";

    /**
     * 消息文本能显示的最大长度:{@value}，超出部分用省略号替代
     */
    public static final int MAX_MSG_LENGTH = 150;

    /**
     * Url链接地址能显示的最大长度:{@value}，超出部分用省略号替代
     */
    public static final int MAX_URL_LENGTH = 50;

    public static final float ZERO = 0.0f;

    /**
     * 空白文件的大小: {@value}
     */
    public static final long ZERO_BYTES = 0L;

    public static final String PERCENTAGE = "%";

    /**
     * UK转运保留编号:{@value}
     */
    public static final String UKTRANSFER_RESERVED_ID = "77";

    /**
     * 统一做单保留编号:{@value}
     */
    public static final String CENTRAL_FULFILL_RESERVED_ID = "290";

    /**
     * US Purchase Back and Transfer Reserved Account Id: {@value}
     */
    public static final String PURCHASEBACK_RESERVED_ID = "120";

    /**
     * 时间描述后缀，形如8:00等等
     */
    public static final String TIME_POSTFIX = ":00";

    /**
     * 某些关键操作出现异常时，重复操作的最大次数: {@value}
     */
    public static final int MAX_REPEAT_TIMES = 3;

    /**
     * 版本更新日期格式
     */
    public static final FastDateFormat UPDATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    /**
     * Fallback text for none entered data or source
     */
    public static final String NONE_ENTERED = "None entered";

    /**
     * Dummy Gmail Address
     */
    public static final String DUMMY_EMAIL = "dummy@gmail.com";

    public static final int HUNDRED = 100;

    public static final int HOURS_ONE_DAY = 24;

    public static final int HOURS_HALF_DAY = 12;

    public static final String GOOGLE_API_CREDENTIAL_FILENAME = "StoredCredential";

    /**
     * WebDriver Implicit Wait默认超时时间, 单位为毫秒
     */
    public static final long DEFAULT_DRIVER_TIME_OUT = 6000;
}
