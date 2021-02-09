package com.kktt.jesus.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class VariationUtil {

    public static String matchVariationTheme(JSONObject properties, JSONArray enableThemeValues) {
        String colorKey = "Color";
        String sizeKey = "Size";
        boolean containsColorProperty = properties.containsKey(colorKey);
        boolean containsSizeProperty = properties.containsKey(sizeKey);
        String matchTheme = null;
        for (int i = 0; i < enableThemeValues.size(); i++) {
            String enableThemeValue = enableThemeValues.getString(i);

            boolean enableThemeValueContainsColorKey = enableThemeValue.toLowerCase().contains(colorKey.toLowerCase());
            boolean enableThemeValueContainsSizeKey = enableThemeValue.toLowerCase().contains(sizeKey.toLowerCase());

            boolean matchThemeValueContainsColorKey = enableThemeValue.toLowerCase().equals(colorKey.toLowerCase())
                    || enableThemeValue.toLowerCase().equals(colorKey.toLowerCase() + "name");
            boolean matchThemeValueContainsSizeKey = enableThemeValue.toLowerCase().equals(sizeKey.toLowerCase())
                    || enableThemeValue.toLowerCase().equals(sizeKey.toLowerCase() + "name");
            boolean matchThemeValueContainsColorAndSizeKey = enableThemeValue.toLowerCase().equals(colorKey.toLowerCase() + "-" + sizeKey.toLowerCase())
                    || enableThemeValue.toLowerCase().equals(sizeKey.toLowerCase() + "-" + colorKey.toLowerCase())
                    || enableThemeValue.toLowerCase().equals(colorKey.toLowerCase() + " & " + sizeKey.toLowerCase())
                    || enableThemeValue.toLowerCase().equals(sizeKey.toLowerCase() + " & " + colorKey.toLowerCase());
            if (containsColorProperty && containsSizeProperty) {
                if (matchThemeValueContainsColorAndSizeKey)
                    return enableThemeValue;
                else if (enableThemeValueContainsColorKey && enableThemeValueContainsSizeKey)
                    matchTheme = enableThemeValue;
            } else if (containsColorProperty) {
                if (matchThemeValueContainsColorKey)
                    return enableThemeValue;
                else if (enableThemeValueContainsColorKey)
                    matchTheme = enableThemeValue;
            } else if (containsSizeProperty) {
                if (matchThemeValueContainsSizeKey)
                    return enableThemeValue;
                if (enableThemeValueContainsSizeKey)
                    matchTheme = enableThemeValue;
            }
        }
        return matchTheme;
    }
}
