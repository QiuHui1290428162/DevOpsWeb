package com.lanf.tasks.service;



import com.lanf.common.exception.GlobalExpiredException;

import java.sql.ResultSet;

//储存过程类
public interface StoredProcedureService {

    public String executeStoredProcedure (String storedProcedureName)throws GlobalExpiredException;

    public String exportStoredProcedureToExcel(String storedProcedureName, String taskName, String outputDirectory)throws GlobalExpiredException;

    public void close(ResultSet resultSet) throws GlobalExpiredException;
}