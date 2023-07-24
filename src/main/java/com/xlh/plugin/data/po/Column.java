package com.xlh.plugin.data.po;

/**
 * @author xingluheng
 * @date 2023/07/19 19:41
 **/
public class Column {

    private String comment;
    private String name;
    private int type;
    private boolean id;

    public Column(String comment, String name, int type) {
        this.comment = comment;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public boolean isId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setId(boolean id) {
        this.id = id;
    }

}
