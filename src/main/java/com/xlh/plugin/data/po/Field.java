package com.xlh.plugin.data.po;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang.StringUtils;

/**
 * @author xingluheng
 */
public class Field {

    private String comment;
    private String columnName;
    private Class<?> type;
    private boolean id;

    public Field(String comment, Class<?> type, String columnName) {
        this.comment = comment;
        this.type = type;
        this.columnName = columnName;
    }

    public String getComment() {
        return comment;
    }

    public String getTypeName() {
        return type.getName();
    }

    public String getTypeSimpleName() {
        return type.getSimpleName();
    }

    public String getColumnName() {
        return columnName;
    }

    public String getName() {
        String str = columnName;
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isId() {
        return id;
    }

    public boolean isImport() {
        String typeName = getTypeName();
        return !type.isPrimitive() && !"java.lang".equals(StringUtils.substringBeforeLast(typeName, "."));
    }

}
