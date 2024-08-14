package com.lanf.tasks.service.impl;


import com.lanf.common.exception.CacheExpiredException;
import com.lanf.common.utils.ExcelUtil;
import com.lanf.tasks.service.StoredProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class StoredProcedureServiceImpl implements StoredProcedureService {

    private static final Logger logger = LogManager.getLogger(StoredProcedureServiceImpl.class);

    @Autowired
    private DataSource dataSource;


    /**
     *  执行存储过程,并获取到返回的结果集
     * @param storedProcedureName  储存过程名称
     * @return
     */
    public String executeStoredProcedure(String storedProcedureName) throws CacheExpiredException {
        StringBuilder result = new StringBuilder();

        // 提取存储过程名和参数
        String procedureName = storedProcedureName.split(" ")[0].trim();
        Map<String, String> params = parseParameters(storedProcedureName);

        //通过 DataSourceUtils获取连接，而不是直接注入 DataSource。可以确保在运行时使用动态数据源。
        try (Connection connection = DataSourceUtils.getConnection(dataSource)) {

            // 构建存储过程调用字符串
            StringBuilder callString = new StringBuilder("{call " + procedureName +" (");
            if (!params.isEmpty()) {
                for (String paramName : params.keySet()) {
                    callString.append("?, ");
                }
                callString.setLength(callString.length() - 2); // 去除最后一个逗号和空格
            }
            callString.append(")}");

            logger.info("准备调用存储过程: {}", callString);

            // 准备 CallableStatement
            try (CallableStatement callableStatement = connection.prepareCall(callString.toString())) {
                int index = 1;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    callableStatement.setString(index++, entry.getValue());
                }

                // 注册返回参数
                //callableStatement.registerOutParameter(index, Types.INTEGER);

                // 执行存储过程,判断返回的结果集是否有数据
                boolean hasResults = callableStatement.execute();

                while (true) {
                    if (hasResults) {

                        try (ResultSet resultSet = callableStatement.getResultSet()) {  //获取结果集

                            //获取到元数据
                            ResultSetMetaData metaData = resultSet.getMetaData();
                            //获取列数
                            int columnCount = metaData.getColumnCount();

                            //多列需要排版成表格形式,单列不排版
                            if (columnCount > 1) {
                                // 添加表头
                                result.append("<table style='border: 1px solid black; border-collapse: collapse;'><tr>");
                                for (int i = 1; i <= columnCount; i++) {
                                    result.append("<th style='border: 1px solid black;'>").append(metaData.getColumnName(i)).append("</th>");
                                }
                                result.append("</tr>");

                                // 添加数据行
                                while (resultSet.next()) {
                                    result.append("<tr>");
                                    for (int i = 1; i <= columnCount; i++) {
                                        result.append("<td style='border: 1px solid black;'>").append(resultSet.getString(i)).append("</td>");
                                    }
                                    result.append("</tr>");
                                }
                                result.append("</table><br>");
                            } else {
                                // 处理单列情况
                                while (resultSet.next()) {
                                    result.append(resultSet.getString(1)).append("<br>");
                                }
                            }
                        }
                    }else{
                        int updateCount = callableStatement.getUpdateCount();
                        if (updateCount == -1) {
                            break; // 没有更多的结果集或更新计数，跳出循环
                        }
                    }

                    // 检查是否有更多的结果集
                    hasResults = callableStatement.getMoreResults();
                }
            }
        } catch (SQLException e) {
            // 记录错误日志
            logger.error("{}执行数据库错误: {}", procedureName,e.getMessage(),e);
            throw new CacheExpiredException(101001,"执行数据库错误"+e.getMessage(),"StoredProcedureServiceImpl");

        }
        return result.toString();
    }

    /**
     *  执行存储过程生成Excel文件, 返回文件路径
     * @param storedProcedureName  储存过程名称
     * @param taskName  文件名称
     * @return
     */
    @Override
    public String exportStoredProcedureToExcel(String storedProcedureName, String taskName, String outputDirectory)throws CacheExpiredException {
        String filePath = "";

        // 提取存储过程名和参数
        String procedureName = storedProcedureName.split(" ")[0].trim();
        Map<String, String> params = parseParameters(storedProcedureName);

        logger.info("准备调用存储过程: {}, 参数: {}", procedureName, params);

        //通过 DataSourceUtils获取连接，而不是直接注入 DataSource。可以确保在运行时使用动态数据源。
        try (Connection connection = DataSourceUtils.getConnection(dataSource)) {

            // 构建存储过程调用字符串
            StringBuilder callString = new StringBuilder("{call " + procedureName +" (");
            if (!params.isEmpty()) {
                for (String paramName : params.keySet()) {
                    callString.append("?, ");
                }
                callString.setLength(callString.length() - 2); // 去除最后一个逗号和空格
            }
            callString.append(")}");

            // 准备 CallableStatement
            try (CallableStatement callableStatement = connection.prepareCall(callString.toString())) {
                int index = 1;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    callableStatement.setString(index++, entry.getValue());
                }

                // 执行存储过程,判断返回的结果集是否有数据
                boolean hasResults = callableStatement.execute();

                while (true){
                    if (hasResults) {
                        //获取到第一个结果集
                        try (ResultSet resultSet = callableStatement.getResultSet()) {

                            // 生成 Excel 文件
                            filePath = ExcelUtil.generateExcel(resultSet, taskName, outputDirectory);
                        } catch (IOException e) {
                            // 记录错误日志
                            logger.error("{} IO文件操作错误: {}", procedureName,e.getMessage(),e);
                            throw new CacheExpiredException(100014,"IO文件操作错误: "+e.getMessage(),"StoredProcedureServiceImpl");
                        }
                        break; // 如果找到了结果集，则跳出循环
                    }else {
                        int updateCount = callableStatement.getUpdateCount();
                        if (updateCount == -1) {
                            break; // 如果没有更多结果集和更新计数，则跳出循环
                        }
                    }
                    // 检查是否有更多的结果集
                    hasResults = callableStatement.getMoreResults();
                }
            }

        } catch (SQLException e) {
            // 记录错误日志
            logger.error("{}执行数据库错误: {}", procedureName,e.getMessage(),e);
            throw new CacheExpiredException(101001,"执行数据库错误: "+e.getMessage(),"StoredProcedureServiceImpl");
        }
        return filePath;
    }

    //分离储存过程参数和参数值,并存放在Map中
    public static Map<String, String> parseParameters(String storedProcedureName) {
        Map<String, String> paramMap = new LinkedHashMap<>();

        // 获取参数列表部分，确保从第一个 @ 符号开始分割
        int firstParamIndex = storedProcedureName.indexOf('@');
        if (firstParamIndex == -1) {
            return paramMap; // 没有参数，直接返回空的paramMap
        }
        String paramString = storedProcedureName.substring(firstParamIndex);
        String[] params = paramString.split(",\\s*");

        for (String param : params) {
            String[] keyValue = param.split("=", 2); // 只分割第一次出现的等号
            if (keyValue.length == 2) {
                String paramName = keyValue[0].trim().substring(1); // 去掉前面的 '@' 符号
                String paramValue = keyValue[1].trim();

                // 去除单引号
                if (paramValue.startsWith("N'") && paramValue.endsWith("'")) {
                    paramValue = paramValue.substring(2, paramValue.length() - 1);
                } else if (paramValue.startsWith("'") && paramValue.endsWith("'")) {
                    paramValue = paramValue.substring(1, paramValue.length() - 1);
                }

                paramMap.put(paramName, paramValue);
            }
        }

        return paramMap;
    }

    @Override
    public void close(ResultSet resultSet) throws CacheExpiredException {
        try{
            Statement statement = resultSet.getStatement();
            Connection connection = statement.getConnection();
            resultSet.close();
            statement.close();
            connection.close();
        }catch (SQLException e){
            // 记录错误日志
            logger.error("关闭数据库错误: {}", e.getMessage(),e);
            throw new CacheExpiredException(101001,"执行数据库错误"+e.getMessage(),"DatabaseExecutorService");
        }
    }
}

