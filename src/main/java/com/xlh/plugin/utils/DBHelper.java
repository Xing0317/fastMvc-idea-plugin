package com.xlh.plugin.utils;

import com.xlh.plugin.data.po.Column;
import com.xlh.plugin.data.po.Table;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.*;

/**
 * @author xingluheng
 * @date 2023/07/19 19:38
 **/
public class DBHelper {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String database;

    private Properties properties;

    public DBHelper(String host, Integer port, String username, String password, String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;

        try {

            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        properties = new Properties();
        properties.put("user", this.username);
        properties.put("password", this.password);
        properties.setProperty("remarks", "true");
        properties.put("useInformationSchema", "true");
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false", properties);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Connection getConnection(String database) {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false", properties);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public List<String> getDatabases() {
        Connection conn = getConnection();
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();
            List<String> rs = new ArrayList<>();
            while (catalogs.next()) {
                String db = catalogs.getString("TABLE_CAT");
                if (StringUtils.equalsIgnoreCase(db, "information_schema")) {
                    continue;
                }
                rs.add(db);
            }
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    public Map<String, String> getAllTableName(String database) {
        this.database = database;
        Connection conn = getConnection(this.database);
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(database, null, "%", new String[]{"TABLE"});
            Map<String,String> tableMap = new LinkedHashMap<>(16);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String remarks = rs.getString("REMARKS");
                tableMap.put(tableName,remarks);
            }
            return tableMap;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    public Table getTable(String tableName,String tablePrefix) {
        Connection conn = getConnection(this.database);
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, "", tableName, new String[]{"TABLE"});
            Table table = null;
            if (rs.next()) {
                table = new Table(rs.getString("REMARKS"), tableName.replace(tablePrefix,""), getAllColumn(tableName, conn));
            }
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            closeConnection(conn);
        }
    }

    private List<Column> getAllColumn(String tableName, Connection conn) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            // 获取表中的主键
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
            String primaryKey = null;
            while (primaryKeys.next()) {
                primaryKey = primaryKeys.getString("COLUMN_NAME");
            }
            ResultSet rs = metaData.getColumns(null, null, tableName, null);
            List<Column> ls = new ArrayList<>();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                Column column = new Column(rs.getString("REMARKS"), columnName, rs.getInt("DATA_TYPE"));
                if (columnName.equals(primaryKey)) {
                    column.setId(true);
                }
                ls.add(column);
            }
            return ls;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String testDatabase() {
        Connection conn = getConnection();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT VERSION() AS MYSQL_VERSION");
            ResultSet resultSet = null;
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("MYSQL_VERSION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        return "Err";
    }

    public static void main(String[] args) {
        DBHelper dbHelper = new DBHelper("127.0.0.1", 3306, "root", "Xing0317.", "ruoyi");
        Map<String, String> map = dbHelper.getAllTableName("redis");
        System.out.println(map);
    }

}
