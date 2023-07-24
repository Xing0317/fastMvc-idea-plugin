package com.xlh.plugin.data.vo;

import com.xlh.plugin.data.po.Base;
import com.xlh.plugin.data.po.Table;

import java.util.List;

/**
 * @author xingluheng
 * @date 2023/07/20 10:14
 **/
public class CodeGenContextVO {
    private String daoDir;
    private String pojoDir;
    private String xmlDir;
    private String serviceDir;
    private String controllerDir;
    private List<Table> tables;
    private String author;

    public String getDaoDir() {
        return daoDir;
    }

    public void setDaoDir(String daoDir) {
        this.daoDir = daoDir;
    }

    public String getPojoDir() {
        return pojoDir;
    }

    public void setPojoDir(String pojoDir) {
        this.pojoDir = pojoDir;
    }

    public String getXmlDir() {
        return xmlDir;
    }

    public void setXmlDir(String xmlDir) {
        this.xmlDir = xmlDir;
    }

    public String getServiceDir() {
        return serviceDir;
    }

    public void setServiceDir(String serviceDir) {
        this.serviceDir = serviceDir;
    }

    public String getControllerDir() {
        return controllerDir;
    }

    public void setControllerDir(String controllerDir) {
        this.controllerDir = controllerDir;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public String getAuthor() {
        return Base.author;
    }

    public void setAuthor(String author) {
        Base.author = author;
    }

    @Override
    public String toString() {
        return "CodeGenContextVO{" +
                "daoDir='" + daoDir + '\'' +
                ", pojoDir='" + pojoDir + '\'' +
                ", xmlDir='" + xmlDir + '\'' +
                ", serviceDir='" + serviceDir + '\'' +
                ", controllerDir='" + controllerDir + '\'' +
                '}';
    }

}
