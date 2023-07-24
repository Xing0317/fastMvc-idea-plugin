package com.xlh.plugin.data.po;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xingluheng
 * @date 2023/07/20 17:12
 **/
public class Model extends Base{

    private String tableName;
    private List<Field> fields;

    public Model(String comment, String name, String tableName, List<Field> fields) {
        super(comment, name);
        this.tableName = tableName;
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public Set<String> getImports() {
        Set<String> imports = new HashSet<>();
        List<Field> fields = getFields();
        for (Field field : fields) {
            if (field.isImport()) {
                imports.add(field.getTypeName());
            }
        }
        return imports;
    }
}
