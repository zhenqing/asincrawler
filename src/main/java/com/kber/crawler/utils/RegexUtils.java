package com.kber.crawler.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/21/2016 3:44 PM
 */
public class RegexUtils {
    private static final String AMAZON_ORDER_NO = "[0-9]{3}-[0-9]{7}-[0-9]{7}";
    public static final String LOG_DATE = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";

    public RegexUtils() {
    }

    public static boolean containsRegex(String source, String regex) {
        return containsRegex(source, regex, false);
    }

    public static boolean containsRegex(String source, String regex, boolean caseSensitive) {
        Pattern pattern = caseSensitive ? Pattern.compile(regex) : Pattern.compile(regex, 2);
        Matcher matcher = pattern.matcher(source);
        return matcher.find();
    }

    public static boolean match(String source, String regex) {
        Pattern pattern = Pattern.compile(regex, 2);
        Matcher matcher = pattern.matcher(source);
        return matcher.matches();
    }

    public static String getMatched(String source, String regex) {
        return getMatched(source, regex, true);
    }

    public static String getMatched(String source, RegexUtils.Regex regex) {
        return getMatched(source, regex.val());
    }

    public static String getMatched(String source, String regex, boolean caseSensitive) {
        List result = getMatchedList(source, regex, caseSensitive);
        return CollectionUtils.isNotEmpty(result) ? (String) result.get(0) : "";
    }

    public static List<String> getMatchedList(String source, String regex) {
        return getMatchedList(source, regex, true);
    }

    public static List<String> getMatchedList(String source, RegexUtils.Regex regex) {
        return getMatchedList(source, regex.val(), true);
    }

    private static List<String> getMatchedList(String source, String regex, boolean caseSensitive) {
        Pattern pattern = caseSensitive ? Pattern.compile(regex) : Pattern.compile(regex, 2);
        Matcher matcher = pattern.matcher(source);
        ArrayList result = new ArrayList();

        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }


    public static enum Regex {
        US_ZIP_CODE("\\d{5}([- ]\\d{4})?"),
        YEAR("[0-9]{4}"),
        ACC("[0-9]{2,3}(US|UK|CA|DE|FR|IT|ES|JP|IN|MX|EU)"),
        COMMON_ORDER_SHEET_NAME("[0-9]{1,2}/[0-9]{1,2}"),
        ASIN("\\b[A-Z|0-9]{10}\\b"),
        TRACKING_FILE("[0-9]{8}_[0-9]{6}[.]txt"),
        FLAT_REPORT_FILE("[0-9]{10,14}[.]txt"),
        BLANK("\\s"),
        MONTH("(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)([a-z]{1,6})?"),
        WEEK_DAY("(Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)"),
        VERSION_CODE("[0-9]{1,2}.[0-9]{1,2}-[0-9]{4}-[0-9]{2}-[0-9]{2}.[0-9]{1,2}"),
        NOTE_NO("[0-9]{1,2}.([0-9]{1,2})?"),
        PC_PRIMARY_ID("[0-9]{2,3}"),
        ChromeVersion("[0-9]{1,3}.[0-9]{1,2}.[0-9]{3,5}.[0-9]{2,4}"),
        PC_SECONDARY_ID("[0-9]{1,2}"),
        TRANSFER_LOG("transfer.[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}.csv"),
        RETURNREQUEST_LOG("returnreq.[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}.log"),
        CUSTOMER_LETTER_FILE_NAME("[0-9]{2,3}[A-Z]{2}_[a-z]{1,9}.txt"),
        ADDRESS_NO("([0-9]+)(ST|ND|RD|TH)"),
        AMAZON_BUYING_CHOICE("Amazon.{1}[^:]+:{1}[^:]+:{1}"),
        PUNCTUATION("\\pP|\\pS"),
        DIGITS_MINUS("[0-9-]"),
        NONE_DIGITS_MINUS("[^0-9-]"),
        NON_CURRENCY("[^0-9,.]"),
        NON_ALPHA_LETTERS("[^A-Za-z]"),
        NON_DIGITS("[^0-9]"),
        NON_ALPHA_LETTER_DIGIT("[^A-Za-z0-9]"),
        SHIPPING_FEE_RANGE("[0-9]{1,2}(.[0-9]{1,2})?-[0-9]{1,2}(.[0-9]{1,2})?"),
        PAGINATION("&startIndex=[0-9]{2,3}"),
        AMOUNT("[0-9]+([.,][0-9]{3})?[.,][0-9]{2}"),
        AMOUNT_TXT_IN_REMARK("([0-9]{1,3}(.[0-9]{2})?)?"),
        AMOUNT_TXT("[0-9]{1,3}(.[0-9]{1,8})?"),
        AMAZON_ORDER_NUMBER("[0-9]{3}-[0-9]{7}-[0-9]{7}"),
        BW_ORDER_NUMBER("[0-9]{8}"),
        HALF_ORDER_NUMBER("[0-9]{12}"),
        COMMON_ORDER_NUMBER("[0-9-]+"),
        NEW_TO_GOOD("n[\\s]*to[\\s]*g"),
        AMAZON_US_DUE_DATE(MONTH.val() + " [0-9]{1,2}(, [0-9]{4})?");

        private final String value;

        public String val() {
            return this.value;
        }

        private Regex(String value) {
            this.value = value;
        }

        public boolean isMatched(String source) {
            return RegexUtils.match(source, this.value);
        }

        public boolean isContained(String source) {
            return RegexUtils.containsRegex(source, this.value);
        }
    }
}
