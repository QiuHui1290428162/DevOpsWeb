package com.lanf.common.utils;


import com.lanf.generator.TableVo;
import com.lanf.generator.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DbUtil {
    private String connectionURL = "jdbc:mysql://localhost:3306/base-lf?characterEncoding=utf-8&useSSL=false";
    private String username = "root";
    private String password = "root";
    private String driverName = "com.mysql.cj.jdbc.Driver";

    public static DbUtil n() {
        return new DbUtil();
    }

    public static DbUtil n(String connectionURL, String username, String password, String driverName) {
        return new DbUtil(connectionURL, username, password, driverName);
    }

    public DbUtil() {
    }

    public DbUtil(String connectionURL, String username, String password, String driverName) {
        this.connectionURL = connectionURL;
        this.username = username;
        this.password = password;
        this.driverName = driverName;
    }

    public Connection getConnection() {
        try {
            Class.forName(this.driverName);
            return DriverManager.getConnection(this.connectionURL, this.username, this.password);
        } catch (Exception var2) {
            System.out.println("数据库连接失败");
            throw new RuntimeException("数据库连接失败");
        }
    }

    public void exec(String sql) {
        Connection connection = this.getConnection();
        Statement sta = null;

        try {
            sta = connection.createStatement();
            sta.execute(sql);
        } catch (SQLException var15) {
            throw new RuntimeException(var15);
        } finally {
            if (sta != null) {
                try {
                    sta.close();
                } catch (SQLException var14) {
                    throw new RuntimeException(var14);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException var13) {
                    throw new RuntimeException(var13);
                }
            }

        }

    }

    public List<Map<String, Object>> queryList(String sql) {
        Connection connection = this.getConnection();
        Statement sta = null;
        ResultSet res = null;
        List<Map<String, Object>> list = new ArrayList();

        try {
            sta = connection.createStatement();
            res = sta.executeQuery(sql);
            ResultSetMetaData rmd = res.getMetaData();
            int columnCount = rmd.getColumnCount();

            while(res.next()) {
                Map<String, Object> rowData = new HashMap();

                for(int i = 1; i <= columnCount; ++i) {
                    rowData.put(rmd.getColumnName(i), res.getObject(i));
                }

                list.add(rowData);
            }
        } catch (Exception var18) {
            var18.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }

                if (sta != null) {
                    sta.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (Exception var17) {
                var17.printStackTrace();
            }

        }

        return list;
    }

    public Map<String, Object> getTableInfo(String name, String genType) throws Exception {
        Connection connection = this.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tableResultSet = metaData.getTables(null, null, name, new String[]{"TABLE"});
        Map<String, Object> map = new Hashtable<>();
        ResultSet columnResultSet = null;

        try {
            if (!tableResultSet.next()) {
                throw new RuntimeException("表(" + name + ")在数据库中不存在");
            }

            String tableName = tableResultSet.getString("TABLE_NAME");
            String modelName = Utils.toFirstUpper(Utils.lineToHump(tableName));
            //String tableRemark = tableResultSet.getString("REMARKS");
            String tableRemark = getTableDescription(connection, tableName); // 自定义方法获取表注释
            if ( StringUtil.isNullOrEmptyString(tableRemark) ){
                throw new RuntimeException("表(" + name + ")在数据库中没有注释");
            }

            map.put("tableName", tableName);
            map.put("modelName", modelName);
            map.put("modelName2", Utils.toFirstLower(modelName));
            map.put("tableRemark", tableRemark);

            columnResultSet = metaData.getColumns(null, null, tableName, "%");

            List<TableVo> list = new ArrayList<>();
            List<TableVo> requireList = new ArrayList<>();
            ArrayList<String> checkList = new ArrayList<>();

            while (columnResultSet.next()) {
                String columnName = columnResultSet.getString("COLUMN_NAME");
                String attrName = Utils.lineToHump(columnName);
                String columnType = columnResultSet.getString("TYPE_NAME");
                String dataType = columnType.toLowerCase();
                columnType = Utils.dataToProperty(columnType);
                int datasize = columnResultSet.getInt("COLUMN_SIZE");
                int digits = columnResultSet.getInt("DECIMAL_DIGITS");
                int nullable = columnResultSet.getInt("NULLABLE");
                //String remarks = columnResultSet.getString("REMARKS");
                String columnDescription  = getColumnDescription(connection, tableName, columnName); // 自定义方法获取列注释
                if ( StringUtil.isNullOrEmptyString(columnDescription ) ){
                    throw new RuntimeException("字段(" + columnName + ")在数据表中没有注释");
                }

                TableVo tableVo = new TableVo(columnName, attrName, columnType, dataType, datasize, digits, nullable, columnDescription );

                if ("id".equals(tableVo.getAttrName())) {
                    tableVo.setSort(100);
                }

                if (tableVo.getAttrName() != null && tableVo.getAttrName().indexOf("Id") > -1) {
                    tableVo.setIsContainId("true");
                } else {
                    tableVo.setIsContainId("false");
                }

                if (!"version".equals(tableVo.getColumeName()) && (!"1".equals(genType) || !"treePath".equals(attrName) && !"level".equals(attrName))) {
                    list.add(tableVo);
                }

                checkList.add(attrName);
                Collections.sort(list, Comparator.comparingInt(TableVo::getSort));

                if (nullable == 0 && !"id".equals(attrName) && !"isDeleted".equals(attrName) && !"createTime".equals(attrName) && !"updateTime".equals(attrName) && !"version".equals(attrName) && (!"1".equals(genType) || !"treePath".equals(attrName) && !"level".equals(attrName))) {
                    requireList.add(tableVo);
                }
            }

            StringBuilder err = new StringBuilder();
            if (!checkList.contains("id")) {
                err.append("表(" + name + ")必须添加id(varchar)字段;");
            }

            if (!checkList.contains("isDeleted")) {
                err.append("表(" + name + ")必须添加is_deleted(tinyint类型)字段");
            }

            if (!checkList.contains("createTime")) {
                err.append("表(" + name + ")必须添加create_time(timestamp类型)字段");
            }

            if (!checkList.contains("updateTime")) {
                err.append("表(" + name + ")必须添加update_time(timestamp类型)字段");
            }

            if (!checkList.contains("version")) {
                err.append("表(" + name + ")必须添加version(bigint类型)字段");
            }

            if ("1".equals(genType)) {
                if (!checkList.contains("name")) {
                    err.append("表(" + name + ")必须添加name(varchar类型)字段");
                }

                if (!checkList.contains("sortValue")) {
                    err.append("表(" + name + ")必须添加sort_value(int类型)字段");
                }

                if (!checkList.contains("parentId")) {
                    err.append("表(" + name + ")必须添加parent_id(varchar类型)字段");
                }

                if (!checkList.contains("treePath")) {
                    err.append("表(" + name + ")必须添加tree_path(varchar类型)字段");
                }

                if (!checkList.contains("level")) {
                    err.append("表(" + name + ")必须添加level(tinyint类型)字段");
                }
            }

            if (err.length() > 0) {
                throw new RuntimeException(err.toString());
            }

            map.put("data", list);
            map.put("requireList", requireList);
            return map;
        } finally {
            if (columnResultSet != null) {
                columnResultSet.close();
            }

            if (tableResultSet != null) {
                tableResultSet.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }


    public String getTableType(String tableName) {
        String sql = "select tab_type from sys_form where name='" + tableName + "'";
        List<Map<String, Object>> list = this.queryList(sql);
        if (list != null && list.size() > 0) {
            Map<String, Object> map = (Map)list.get(0);
            return map.get("tab_type") != null ? map.get("tab_type").toString() : "0";
        } else {
            return "0";
        }
    }

    private String getTableDescription(Connection connection, String tableName) throws SQLException {
        // 通过查询系统视图或者其他方式获取表的描述信息
        String sql = "SELECT value FROM fn_listextendedproperty('MS_Description', 'schema', 'dbo', 'table', ?, null, null)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tableName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        }
        return "";
    }

    private String getColumnDescription(Connection connection, String tableName, String columnName) throws SQLException {
        // 通过查询系统视图或者其他方式获取列的描述信息
        String sql = "SELECT value FROM fn_listextendedproperty('MS_Description', 'schema', 'dbo', 'table', ?, 'column', ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tableName);
            pstmt.setString(2, columnName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        }
        return "";
    }
}

