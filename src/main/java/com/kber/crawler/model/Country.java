package com.kber.crawler.model;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 11/15/2016 5:32 PM
 */
public enum Country {
    US("$", "com", "US", "ATVPDKIKX0DER"),
    CA("CDN$", "ca", "CA", "A2EUQ1WTGCTBG2"),
    /**
     * 英国，需要注意其名称(UK)和实际国家代码(GB)不同，其他国家名称和代码一致
     */
    UK("£", "co.uk", "GB", "A1F83G8C2ARO7P"),
    FR("EUR", "fr", "FR", "A13V1IB3VIYZZH"),
    DE("EUR", "de", "DE", "A1PA6795UKMFR9"),
    IT("EUR", "it", "IT", "APJ6JRA9NG5V4"),
    ES("EUR", "es", "ES", "A1RKKUPIHCS9HS"),
    JP("JPY", "co.jp", "JP", "A1VC38T7YXB528"),
    IN("INR", "in", "IN", "A21TJRUUN4KGV"),
    MX("$", "com.mx", "MX", "A1AM78C64UM0Y8");

    public static final Country[] ALL = Country.values();

    private final String currSymbol;

    private final String urlPostfix;

    private final String code;

    private final String marketPlaceId;

    private static final Map lookup =
            new HashMap();

    static {
        //Create reverse lookup hash map
        for (Country c : Country.values())
            lookup.put(c.name(), c);
    }

    public static Country getCountry(String name) {
        return (Country) lookup.get(name);
    }

    public static int getIndex(Country country) {
        Country[] countries = {Country.US, Country.UK, Country.CA, Country.ES, Country.JP, Country.FR, Country.DE, Country.IT, Country.IN};
        List countriesList = Arrays.asList(countries);
        int index = countriesList.indexOf(country);
        return index;
    }

    Country(String currSymbol, String urlPostfix, String code, String marketPlaceId) {
        this.currSymbol = currSymbol;
        this.urlPostfix = urlPostfix;
        this.code = code;
        this.marketPlaceId = marketPlaceId;
    }

    public String getBaseUrl() {
        return "https://www.amazon." + this.urlPostfix;
    }
}
