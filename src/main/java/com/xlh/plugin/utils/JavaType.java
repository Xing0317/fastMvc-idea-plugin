package com.xlh.plugin.utils;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author xingluheng
 * @date 2023/07/19 20:57
 **/
public class JavaType {

    private static final LinkedHashMap<JDBCType, Class> MAP = new LinkedHashMap<>();

    static {
        //字符串类型
        MAP.put(JDBCType.VARCHAR, String.class);
        MAP.put(JDBCType.LONGVARCHAR, String.class);
        MAP.put(JDBCType.CHAR, String.class);
        //整数类型
        MAP.put(JDBCType.INTEGER, Integer.class);
        MAP.put(JDBCType.BIGINT, Long.class);
        MAP.put(JDBCType.SMALLINT, Integer.class);
        MAP.put(JDBCType.TINYINT, Integer.class);
        //浮点类型
        MAP.put(JDBCType.FLOAT, Float.class);
        MAP.put(JDBCType.DOUBLE, Double.class);
        MAP.put(JDBCType.DECIMAL, BigDecimal.class);
        //其他类型
        MAP.put(JDBCType.BOOLEAN, Boolean.class);
        MAP.put(JDBCType.DATE, Date.class);
        MAP.put(JDBCType.TIME, Date.class);
        MAP.put(JDBCType.TIMESTAMP, Date.class);
        MAP.put(JDBCType.BIT, boolean.class);
    }

    public static Class convertType(int sqlType) {
        return MAP.getOrDefault(JDBCType.valueOf(sqlType), Object.class);
    }

}
