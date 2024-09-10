package com.lanf.common.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelUtil {

    /**
     *
     * @param resultSet       数据库结果集
     * @param tableName       表名称
     * @param outputDirectory 输出目录
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static String generateExcel(ResultSet resultSet, String tableName,String outputDirectory) throws SQLException, IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Result");

        // 表格名称作为第一行，合并居中，加粗，大小为24
        Row tableNameRow = sheet.createRow(0);
        Cell tableNameCell = tableNameRow.createCell(0);
        tableNameCell.setCellValue(tableName);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 24);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        tableNameCell.setCellStyle(style);
        // 获取列数
        int col = resultSet.getMetaData().getColumnCount();

        // 如果列数大于1，则合并单元格
        if (col > 1) {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        }


        // 创建表头
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        Row headerRow = sheet.createRow(1);

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 1; i <= columnCount; i++) {
            Cell cell = headerRow.createCell(i - 1);
            cell.setCellValue(metaData.getColumnName(i));
            cell.setCellStyle(headerStyle);
        }

        // 填充数据行
        int rowNum = 2;
        while (resultSet.next()) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = row.createCell(i - 1);
                cell.setCellValue(resultSet.getString(i));
            }
        }

        // 自动调整列宽，最小宽度为15px
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            int currentWidth = sheet.getColumnWidth(i);
            int minWidth = 15 * 256; // 15 characters width
            if (currentWidth < minWidth) {
                sheet.setColumnWidth(i, minWidth);
            }
        }

        // 生成文件名，包含时间后缀
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = timestamp + "_" + tableName + ".xlsx";
        String filePath = outputDirectory + "/" + fileName;

        // 写入文件
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        // 关闭工作簿
        workbook.close();

        return filePath;
    }
}
