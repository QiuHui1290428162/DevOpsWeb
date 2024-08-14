package com.lanf.tasks.service;



import com.lanf.common.exception.CacheExpiredException;

import java.sql.ResultSet;

//储存过程类
public interface StoredProcedureService {

    public String executeStoredProcedure (String storedProcedureName)throws CacheExpiredException;

    public String exportStoredProcedureToExcel(String storedProcedureName, String taskName, String outputDirectory)throws CacheExpiredException;

    public void close(ResultSet resultSet) throws CacheExpiredException;
}