package com.xlh.plugin.utils;

import com.google.common.base.CaseFormat;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 转换实体类为SQL
 *
 * @author xingluheng
 * @date 2023/07/21 14:23
 **/
public class SqlUtils {
    /**
     * PSI文件转换为SQL
     * @param entityClass
     * @param tableName
     * @return
     */
    public static String convertToMySQL(PsiClass entityClass, String tableName) {
        StringBuilder sql = new StringBuilder();

        // 获取表名
        if (StringUtils.isEmpty(tableName)) {
            tableName = Optional.ofNullable(entityClass.getName()).orElse("Test").toLowerCase();
        }
        sql.append("CREATE TABLE ").append(tableName).append(" (").append("\n");

        // 获取所有属性（包括父类）
        List<PsiField> fields = getAllFields(entityClass);
        List<String> columnDefinitions = getColumnDefinitions(fields);
        List<String> columnComments = getColumnComments(fields);

        // 拼接列定义和注释
        for (int i = 0; i < columnDefinitions.size(); i++) {
            sql.append("  ").append(columnDefinitions.get(i))
                    .append(" COMMENT '")
                    .append(columnComments.get(i))
                    .append("'");
            sql.append(",");
            sql.append("\n");
        }

        String primarySql = "PRIMARY KEY(id)";
        String engin = "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT=";
        sql.append("  ").append(primarySql).append("\n").append(")").append(engin).append("'")
                .append(getTableComment(entityClass)).append("';");

        return sql.toString();
    }

    @Nullable
    private static String getTableComment(PsiClass entityClass) {
        String tableComment = entityClass.getName();
        PsiDocComment docComment = entityClass.getDocComment();
        if (docComment!=null){
            tableComment = fixComment(docComment.getText());
            if (tableComment.contains("*")){
                tableComment = tableComment.substring(0,tableComment.indexOf("*"));
            }
        }
        return tableComment;
    }

    private static List<PsiField> getAllFields(PsiClass entityClass) {
        List<PsiField> fields = new ArrayList<>();

        // 获取当前类的所有属性
        PsiField[] ownFields = entityClass.getAllFields();
        for (PsiField field : ownFields) {
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            fields.add(field);
        }

        // 获取父类的所有属性
        PsiClass superClass = entityClass.getSuperClass();
        if (superClass != null) {
            PsiField[] parentFields = superClass.getAllFields();
            for (PsiField field : parentFields) {
                if (field.getName().equals("serialVersionUID")) {
                    continue;
                }
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * 获取列类型
     *
     * @param fields 属性列表
     * @return 列类型列表
     */
    private static List<String> getColumnDefinitions(List<PsiField> fields) {
        List<String> columnDefinitions = new ArrayList<>();

        for (PsiField field : fields) {
            StringBuilder columnDefinition = new StringBuilder();
            // 获取属性名称
            String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
            columnDefinition.append(columnName).append(" ");
            // 根据属性类型设置列类型
            PsiType type = field.getType();
            if (type.equalsToText("java.lang.Integer") || type.equalsToText("int")) {
                columnDefinition.append("INT");
            } else if (type.equalsToText("java.lang.Long") || type.equalsToText("long")) {
                columnDefinition.append("BIGINT");
            } else if (type.equalsToText("java.lang.Double") || type.equalsToText("double")) {
                columnDefinition.append("DOUBLE");
            } else if (type.equalsToText("java.lang.String")) {
                columnDefinition.append("VARCHAR(50)");
            } else if (type.equalsToText("java.lang.Boolean") || type.equalsToText("boolean")) {
                columnDefinition.append("BOOLEAN");
            } else if (type.equalsToText("java.util.Date")) {
                columnDefinition.append("DATETIME");
            } else if (type.equalsToText("java.math.BigDecimal")){
                columnDefinition.append("DECIMAL(19,2)");
            } else {
                columnDefinition.append("?");
            }
            // 防止子类包含父类元素建表SQL出错
            if (!columnDefinitions.contains(columnDefinition.toString())) {
                columnDefinitions.add(columnDefinition.toString());
            }
        }

        return columnDefinitions;
    }


    private static List<String> getColumnComments(List<PsiField> fields) {
        List<String> columnComments = new ArrayList<>();
        for (PsiField field : fields) {
            // 获取属性注释
            PsiDocComment docComment = field.getDocComment();
            // 添加到列表
            columnComments.add(docComment == null ? "" : fixComment(docComment.getText()));
        }
        return columnComments;
    }

    private static String fixComment(String originComment){
        originComment = originComment.replaceAll("(\\r\\n|\\n|\\\\n|\\s)", "");
        // 起始标记
        String startTag = "/***";
        // 结束标记
        String endTag = "*/";
        int startIndex = originComment.indexOf(startTag);
        int endIndex = originComment.lastIndexOf(endTag);

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return originComment.substring(startIndex + startTag.length(), endIndex).trim();
        }
        return "";
    }

}

