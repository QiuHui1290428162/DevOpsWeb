package com.lanf.tasks.service;



import com.lanf.common.exception.CacheExpiredException;

import java.sql.ResultSet;

public interface DatabaseExecutorService {

    public String executeQuery(String query) throws CacheExpiredException;

    public String executeQueryToExcel(String query, String taskName, String outputDirectory) throws CacheExpiredException;

    public int executeUpdate(String update) throws CacheExpiredException;

    public void close(ResultSet resultSet) throws CacheExpiredException;
}
