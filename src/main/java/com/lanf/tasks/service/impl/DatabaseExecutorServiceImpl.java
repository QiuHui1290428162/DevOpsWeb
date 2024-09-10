package com.lanf.tasks.service.impl;

import com.lanf.common.exception.GlobalExpiredException;
import com.lanf.common.utils.ExcelUtil;
import com.lanf.tasks.service.DatabaseExecutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

@Service
public class DatabaseExecutorServiceImpl implements DatabaseExecutorService {

    private static final Logger logger = LogManager.getLogger(DatabaseExecutorServiceImpl.class);

    @Autowired
    private DataSource dataSource;


    /**
     * 执行查询语句并返回字符串
     *
     * @param query SQL查询语句
     * @return ResultSet 结果集
     * @throws GlobalExpiredException SQL异常
     */
    @Override
    public String executeQuery(String query) throws GlobalExpiredException {
        StringBuilder result = new StringBuilder();
        logger.info("准备执行查询语句: {}", query);
        try{
            //通过 DataSourceUtils获取连接，而不是直接注入 DataSource。可以确保在运行时使用动态数据源。
            Connection connection = DataSourceUtils.getConnection(dataSource);
            Statement statement = connection.createStatement();
            //获取到结果集
            ResultSet resultSet = statement.executeQuery(query);

            //获取到元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取列数
            int columnCount = metaData.getColumnCount();

            if (resultSet.next()) {
                // 添加表头
                result.append("<table style='border: 1px solid black; border-collapse: collapse;'><tr>");
                for (int i = 1; i <= columnCount; i++) {
                    result.append("<th style='border: 1px solid black;'>").append(metaData.getColumnName(i)).append("</th>");
                }
                result.append("</tr>");

                // 添加数据行
                do{
                    result.append("<tr>");
                    for (int i = 1; i <= columnCount; i++) {
                        result.append("<td style='border: 1px solid black;'>").append(resultSet.getString(i)).append("</td>");
                    }
                    result.append("</tr>");
                } while (resultSet.next());
                result.append("</table><br>");
                close(resultSet);
            }else{
                logger.info("执行查询语句未查询到结果集");
            }
        }catch (SQLException e){
            // 记录错误日志
            logger.error("关闭数据库错误: {}", e.getMessage(),e);
            throw new GlobalExpiredException(101001,"执行数据库错误"+e.getMessage(),"DatabaseExecutorService");
        }

        return result.toString();
    }

    /**
     * 执行查询语句并返回字符串
     *
     * @param query SQL查询语句
     * @return ResultSet 结果集
     * @throws GlobalExpiredException SQL异常
     */
    @Override
    public String executeQueryToExcel(String query, String taskName, String outputDirectory) throws GlobalExpiredException {
        String filePath = "";
        logger.info("准备执行查询语句: {}", query);
        try{
            //通过 DataSourceUtils获取连接，而不是直接注入 DataSource。可以确保在运行时使用动态数据源。
            Connection connection = DataSourceUtils.getConnection(dataSource);
            Statement statement = connection.createStatement();
            //获取到结果集
            ResultSet resultSet = statement.executeQuery(query);

            // 生成 Excel 文件
            filePath = ExcelUtil.generateExcel(resultSet, taskName, outputDirectory);

            close(resultSet);
        }catch (SQLException e){
            // 记录错误日志
            logger.error("关闭数据库错误: {}", e.getMessage(),e);
            throw new GlobalExpiredException(101001,"执行数据库错误"+e.getMessage(),"DatabaseExecutorService");
        } catch (IOException e) {
            // 记录错误日志
            logger.error("{} IO文件操作错误: {}", e.getMessage(),e);
            throw new GlobalExpiredException(100014,"IO文件操作错误: "+e.getMessage(),"StoredProcedureServiceImpl");
        }

        return filePath;
    }

    /**
     * 执行更新或插入语句
     *
     * @param update SQL更新或插入语句
     * @return int 受影响的行数
     * @throws GlobalExpiredException SQL异常
     */
    @Override
    public int executeUpdate(String update) throws GlobalExpiredException {
//        try (Connection connection = dataSource.getConnection();
//             Statement statement = connection.createStatement()) {
//            return statement.executeUpdate(update);
//        }
        return 0;
    }

    /**
     * 关闭结果集和相关资源
     *
     * @param resultSet ResultSet 结果集
     * @throws GlobalExpiredException SQL异常
     */
    @Override
    public void close(ResultSet resultSet) throws GlobalExpiredException {
        try{
            Statement statement = resultSet.getStatement();
            Connection connection = statement.getConnection();
            resultSet.close();
            statement.close();
            connection.close();
        }catch (SQLException e){
            // 记录错误日志
            logger.error("关闭数据库错误: {}", e.getMessage(),e);
            throw new GlobalExpiredException(101001,"执行数据库错误"+e.getMessage(),"DatabaseExecutorService");
        }
    }
}

