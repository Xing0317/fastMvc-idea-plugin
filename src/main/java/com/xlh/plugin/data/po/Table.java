package com.xlh.plugin.data.po;

import java.util.List;

/**
 * @author xingluheng
 * @date 2023/07/19 19:42
 **/
public class Table {

    private String comment;
    private String name;
    private List<Column> columns;

    public Table(String comment, String name, List<Column> columns) {
        this.comment = comment;
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public List<Column> getColumns() {
        return columns;
    }

}
