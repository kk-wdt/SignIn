package com.kktt.jesus.utils;

import com.tapcash.tool4seller.library.sellercentral.Constant;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


/**
 * Package com.tapcash.amazon.utils
 *
 * @author Lancer He <lancer.he@gmail.com>
 */
public class MwsUtil extends com.tapcash.tool4seller.library.sellercentral.MwsUtil {

    public static ZonedDateTime getZonedDateTimeToLastHours(int hours, ZoneId zoneId) {
        return ZonedDateTime.now(zoneId).withMinute(0).withSecond(0).withNano(0).minusHours(hours);
    }

    public static ZonedDateTime getZonedDateTimeToLastDays(int days, ZoneId zoneId) {
        return ZonedDateTime.now(zoneId).withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(days);
    }

    public static XMLGregorianCalendar getXmlCalendarInMillis(long millis, ZoneId zoneId) {
        try {
            long curMillis = Instant.now().toEpochMilli() - Duration.ofMinutes(10).toMillis();
            if (millis > curMillis) millis = curMillis;

            GregorianCalendar beforeCalendar = new GregorianCalendar(TimeZone.getTimeZone(zoneId));
            beforeCalendar.setTimeInMillis(millis);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(beforeCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMarketplaceIdFromMarketplaceName(String marketplaceName) {
        if (marketplaceName.equalsIgnoreCase("amazon.com")) return Constant.MARKETPLACE_ID_US;
        if (marketplaceName.equalsIgnoreCase("amazon.ca")) return Constant.MARKETPLACE_ID_CA;
        if (marketplaceName.equalsIgnoreCase("amazon.com.mx")) return Constant.MARKETPLACE_ID_MX;
        if (marketplaceName.equalsIgnoreCase("amazon.co.jp")) return Constant.MARKETPLACE_ID_JP;
        if (marketplaceName.equalsIgnoreCase("amazon.co.uk")) return Constant.MARKETPLACE_ID_UK;
        if (marketplaceName.equalsIgnoreCase("amazon.fr")) return Constant.MARKETPLACE_ID_FR;
        if (marketplaceName.equalsIgnoreCase("amazon.de")) return Constant.MARKETPLACE_ID_DE;
        if (marketplaceName.equalsIgnoreCase("amazon.es")) return Constant.MARKETPLACE_ID_ES;
        if (marketplaceName.equalsIgnoreCase("amazon.it")) return Constant.MARKETPLACE_ID_IT;
        if (marketplaceName.equalsIgnoreCase("amazon.in")) return Constant.MARKETPLACE_ID_IN;
        if (marketplaceName.equalsIgnoreCase("amazon.nl")) return Constant.MARKETPLACE_ID_NL;
        if (marketplaceName.equalsIgnoreCase("amazon.com.au")) return Constant.MARKETPLACE_ID_AU;
        if (marketplaceName.equalsIgnoreCase("amazon.ae")) return Constant.MARKETPLACE_ID_AE;
        return "";
    }

    public static String getMarketplaceIdFromCurrencyCode(String currencyCode) {
        if (currencyCode.equalsIgnoreCase("USD")) return Constant.MARKETPLACE_ID_US;
        if (currencyCode.equalsIgnoreCase("GBP")) return Constant.MARKETPLACE_ID_UK;
        if (currencyCode.equalsIgnoreCase("JPY")) return Constant.MARKETPLACE_ID_JP;
        if (currencyCode.equalsIgnoreCase("CAD")) return Constant.MARKETPLACE_ID_CA;
        if (currencyCode.equalsIgnoreCase("INR")) return Constant.MARKETPLACE_ID_IN;
        if (currencyCode.equalsIgnoreCase("MXN")) return Constant.MARKETPLACE_ID_MX;
        if (currencyCode.equalsIgnoreCase("AUD")) return Constant.MARKETPLACE_ID_AU;
        return null;
    }

    public static String getCurrencyCodeFromMarketplaceId(String marketplaceId) {
        if (marketplaceId.equalsIgnoreCase(Constant.MARKETPLACE_ID_US)) return "USD";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_UK)) return "GBP";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_JP)) return "JPY";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_CA)) return "CAD";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_IN)) return "INR";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_MX)) return "MXN";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_AU)) return "AUD";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_FR)) return "EUR";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_DE)) return "EUR";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_IT)) return "EUR";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_ES)) return "EUR";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_NL)) return "EUR";
        return null;
    }

    public static String getSiteFromMarketplaceId(String marketplaceId) {
        if (marketplaceId.equalsIgnoreCase(Constant.MARKETPLACE_ID_US)) return "US";
        if (marketplaceId.equalsIgnoreCase( Constant.MARKETPLACE_ID_CA)) return "CA";
        return null;
    }


    private static Map<String, String> amazonCurrencyCodeMap = new HashMap<>();

    static {
        amazonCurrencyCodeMap.put(Constant.MARKETPLACE_ID_CA, "CAD");
        amazonCurrencyCodeMap.put(Constant.MARKETPLACE_ID_US, "USD");
        amazonCurrencyCodeMap.put(Constant.MARKETPLACE_ID_UK, "GBP");
        amazonCurrencyCodeMap.put(Constant.MARKETPLACE_ID_JP, "JPY");
    }

    public static String getAmazonCurrencyCode(String marketplaceId) {
        String currencyCode = amazonCurrencyCodeMap.get(marketplaceId);
        if (StringUtils.isEmpty(currencyCode)) {
            return "DEFAULT";
        }
        return currencyCode;
    }
}