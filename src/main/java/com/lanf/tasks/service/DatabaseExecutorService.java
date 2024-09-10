package com.lanf.tasks.service;



import com.lanf.common.exception.GlobalExpiredException;

import java.sql.ResultSet;

public interface DatabaseExecutorService {

    public String executeQuery(String query) throws GlobalExpiredException;

    public String executeQueryToExcel(String query, String taskName, String outputDirectory) throws GlobalExpiredException;

    public int executeUpdate(String update) throws GlobalExpiredException;

    public void close(ResultSet resultSet) throws GlobalExpiredException;
}
