package com.kktt.jesus.utils;

import com.tapcash.tool4seller.library.sellercentral.Constant;
import jodd.util.Format;
import jodd.util.StringUtil;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EntityUtil {

    private static ConcurrentHashMap<String, String> InsertSqlMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> ReplaceSqlMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> UpdateSqlMap = new ConcurrentHashMap<>();

    private static final Set<String> EU_MARKETPLACE_IDS;

    static {
        EU_MARKETPLACE_IDS = new HashSet<>();
        EU_MARKETPLACE_IDS.add(Constant.MARKETPLACE_ID_IT);
        EU_MARKETPLACE_IDS.add(Constant.MARKETPLACE_ID_DE);
        EU_MARKETPLACE_IDS.add(Constant.MARKETPLACE_ID_ES);
        EU_MARKETPLACE_IDS.add(Constant.MARKETPLACE_ID_FR);
    }

    private static List<String> getAllPropertyNames(Class<?> clazz) {
        List<String> list = new ArrayList<>();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            String name = descriptor.getName();
            if (name.equals("class"))
                continue;

            try {
                if (null != clazz.getDeclaredField(name).getAnnotation(ReadOnlyProperty.class))
                    continue;
            } catch (NoSuchFieldException e) {
                continue;
            }

            list.add(name);
        }
        return list;
    }

    public static String genInsertSql(String tableName, Class<?> entityClazz) {
        String key = tableName + entityClazz.getSimpleName();
        String cacheSql = InsertSqlMap.get(key);
        if (cacheSql != null) return cacheSql;

        StringBuilder sbField = new StringBuilder();
        StringBuilder sbParam = new StringBuilder();

        List<String> propertyNames = getAllPropertyNames(entityClazz);
        for (String propertyName : propertyNames) {
            String name = Format.fromCamelCase(propertyName, '_');
            sbField.append('`').append(name).append('`').append(",");
            sbParam.append(":").append(propertyName).append(",");
        }

        if (sbField.length() > 0) sbField.deleteCharAt(sbField.length() - 1);
        if (sbParam.length() > 0) sbParam.deleteCharAt(sbParam.length() - 1);

        String sql = "insert into %s(%s) values(%s)";
        sql = String.format(sql, tableName, sbField.toString(), sbParam.toString());
        InsertSqlMap.put(key, sql);
        return sql;
    }

    public static String genReplaceSql(String tableName, Class<?> entityClazz) {
        String key = tableName + entityClazz.getSimpleName();
        String cacheSql = ReplaceSqlMap.get(key);
        if (cacheSql != null) return cacheSql;

        StringBuilder sbField = new StringBuilder();
        StringBuilder sbParam = new StringBuilder();

        List<String> propertyNames = getAllPropertyNames(entityClazz);
        for (String propertyName : propertyNames) {
            String name = Format.fromCamelCase(propertyName, '_');
            sbField.append('`').append(name).append('`').append(",");
            sbParam.append(":").append(propertyName).append(",");
        }

        if (sbField.length() > 0) sbField.deleteCharAt(sbField.length() - 1);
        if (sbParam.length() > 0) sbParam.deleteCharAt(sbParam.length() - 1);

        String sql = "replace into %s(%s) values(%s)";
        sql = String.format(sql, tableName, sbField.toString(), sbParam.toString());

        ReplaceSqlMap.put(key, sql);
        return sql;
    }

    public static String genUpdateSql(String tableName, Class<?> entityClazz, String[] keyFields) {
        String key = tableName + entityClazz.getSimpleName() + keyFields.toString();
        String cacheSql = UpdateSqlMap.get(key);
        if (cacheSql != null) return cacheSql;

        StringBuilder sbCondition = new StringBuilder();

        List<String> propertyNames = getAllPropertyNames(entityClazz);
        int count = 0;
        for (String propertyName : propertyNames) {
            for (String keyField : keyFields) {
                String keyProperty = Format.toCamelCase(keyField, false, '_');
                if (propertyName.equalsIgnoreCase(keyProperty)) {
                    count ++;
                    String name = Format.fromCamelCase(propertyName, '_');
                    if(count == keyFields.length){
                        sbCondition.append('`').append(name).append('`').append(" = :").append(keyProperty);
                    }else{
                        sbCondition.append('`').append(name).append('`').append(" = :").append(keyProperty).append(" and ");
                    }
                    break;
                }
            }
        }

        if (sbCondition.length() == 0)
            throw new RuntimeException("keyField not found in " + entityClazz);

        StringBuilder sbField = new StringBuilder();
        for (String propertyName : propertyNames) {
            String name = Format.fromCamelCase(propertyName, '_');
            sbField.append('`').append(name).append('`').append(" = :").append(propertyName).append(",");
        }

        if (sbField.length() > 0) sbField.deleteCharAt(sbField.length() - 1);

        String sql = "update %s set %s where %s";
        sql = String.format(sql, tableName, sbField.toString(), sbCondition.toString());

        UpdateSqlMap.put(key, sql);
        return sql;
    }


    public static String genUpdateSql(String tableName, Class<?> entityClazz, String keyField) {
        String key = tableName + entityClazz.getSimpleName() + keyField;
        String cacheSql = UpdateSqlMap.get(key);
        if (cacheSql != null) return cacheSql;

        String keyProperty = Format.toCamelCase(keyField, false, '_');
        StringBuilder sbCondition = new StringBuilder();

        List<String> propertyNames = getAllPropertyNames(entityClazz);
        for (String propertyName : propertyNames) {
            if (propertyName.equalsIgnoreCase(keyProperty)) {
                String name = Format.fromCamelCase(propertyName, '_');
                sbCondition.append('`').append(name).append('`').append(" = :").append(keyProperty);
                break;
            }
        }

        if (sbCondition.length() == 0)
            throw new RuntimeException("keyField not found in " + entityClazz);

        StringBuilder sbField = new StringBuilder();
        for (String propertyName : propertyNames) {
            String name = Format.fromCamelCase(propertyName, '_');
            sbField.append('`').append(name).append('`').append(" = :").append(propertyName).append(",");
        }

        if (sbField.length() > 0) sbField.deleteCharAt(sbField.length() - 1);

        String sql = "update %s set %s where %s";
        sql = String.format(sql, tableName, sbField.toString(), sbCondition.toString());

        UpdateSqlMap.put(key, sql);
        return sql;
    }

    public static String genUpdateSql(String tableName, Class<?> entityClazz) {
        String key = tableName + entityClazz.getSimpleName();
        String cacheSql = UpdateSqlMap.get(key);
        if (cacheSql != null) return cacheSql;

        StringBuilder sbField = new StringBuilder();
        List<String> propertyNames = getAllPropertyNames(entityClazz);
        for (String propertyName : propertyNames) {
            String name = Format.fromCamelCase(propertyName, '_');
            sbField.append(name).append("=:").append(propertyName).append(",");
        }

        if (sbField.length() > 0) sbField.deleteCharAt(sbField.length() - 1);

        String sql = " update %s set %s ";
        sql = String.format(sql, tableName, sbField.toString());

        UpdateSqlMap.put(key, sql);
        return sql;
    }

    public static String genUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String normalizeStringValue(String value) {
        if (StringUtil.isEmpty(value)) return null;
        return value.trim();
    }

    public static Float normalizeFloatValue(String value, String marketplaceId) {
        if (StringUtil.isEmpty(value)) return null;

        if (EU_MARKETPLACE_IDS.contains(marketplaceId)) value = StringUtil.replace(value, ",", ".");
        else value = StringUtil.remove(value.trim(), ",");

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    public static Integer normalizeIntegerValue(String value) {
        if (StringUtil.isEmpty(value)) return null;
        return Integer.parseInt(StringUtil.remove(value.trim(), ","));
    }
}
