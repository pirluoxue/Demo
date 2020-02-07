package com.example.demo.util.io;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen_bq
 * @description poi工具类
 * @create: 2019/10/18 15:18
 **/
public class PoiUtils {

    private static final String FILE_FORMAT_XLSX = ".xlsx";
    private static final String FORMAT_REGEX = "(\\..*)";
    private static final String REPLACE_REGEX = "$1";
    private static final String DEFAULT_TABLE_FIRST_PAGE_NAME = "表1";
    private static final boolean AUTO_SIZE_COLUMN = true;
    private static final int DEFAULT_DECORATION_COLUMN_NUM = 20;

    private static void create() {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("表1");
        CellRangeAddress region = new CellRangeAddress(1, 4, 2, 5);
        XSSFRow row = sheet.createRow(1);
        row.createCell(2).setCellValue("第二至五行3-6列合并");
        sheet.addMergedRegion(region);

        XSSFCellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
        cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
        cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中

        XSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");// 设置字体名称
        font.setFontHeightInPoints((short)10);// 设置字号
        font.setColor(IndexedColors.BLACK.index);// 设置字体颜色

        cellStyle.setFont(font);

        CellUtil.getCell(row, 2).setCellStyle(cellStyle);

        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, region, sheet);

        RegionUtil.setBottomBorderColor(IndexedColors.BLUE.index, region, sheet);
        RegionUtil.setLeftBorderColor(IndexedColors.BLUE.index, region, sheet);
        RegionUtil.setRightBorderColor(IndexedColors.BLUE.index, region, sheet);
        RegionUtil.setTopBorderColor(12, region, sheet);

        workbook.cloneSheet(0, "表2");

        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream("E:\\work\\工作文档\\test.xlsx");
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("写出成功！");
    }

    public void mergeCell(XSSFSheet sheet) {
        CellRangeAddress region = new CellRangeAddress(1, 4, 2, 5);
        XSSFRow row = sheet.createRow(1);
        row.createCell(2).setCellValue("第二至五行3-6列合并");
        sheet.addMergedRegion(region);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, region, sheet);

        RegionUtil.setBottomBorderColor(IndexedColors.BLUE.index, region, sheet);
        RegionUtil.setLeftBorderColor(IndexedColors.BLUE.index, region, sheet);
        RegionUtil.setRightBorderColor(IndexedColors.BLUE.index, region, sheet);
        RegionUtil.setTopBorderColor(12, region, sheet);
    }

    public static <T> void exportExcel(List<T> dataList, String fullFileName) {
        if (dataList == null || dataList.size() <= 0) {
            return;
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        if (dataList.get(0) instanceof List && ((List)dataList.get(0)).get(0) instanceof String) {
            buildXSSFWorkbook(workbook, (List<List<String>>)dataList);
        }

        FileOutputStream fileOut;
        try {
            if (!fullFileName.endsWith(FILE_FORMAT_XLSX)) {
                fullFileName = fullFileName.replaceAll(FORMAT_REGEX, "");
                fullFileName += FILE_FORMAT_XLSX;
            }
            File file = new File(fullFileName);
            if (file.exists()) {
                fullFileName = fullFileName.replaceAll(FORMAT_REGEX, "_tmp" + REPLACE_REGEX);
            }
            fileOut = new FileOutputStream(fullFileName);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void exportExcelForLarge(List<T> dataList, String fullFileName) {
        if (dataList == null || dataList.size() <= 0) {
            return;
        }
        SXSSFWorkbook workbook = new SXSSFWorkbook ();
        if (dataList.get(0) instanceof List && ((List)dataList.get(0)).get(0) instanceof String) {
            buildXSSFWorkbook(workbook, (List<List<String>>)dataList);
        }

        FileOutputStream fileOut;
        try {
            if (!fullFileName.endsWith(FILE_FORMAT_XLSX)) {
                fullFileName = fullFileName.replaceAll(FORMAT_REGEX, "");
                fullFileName += FILE_FORMAT_XLSX;
            }
            File file = new File(fullFileName);
            if (file.exists()) {
                fullFileName = fullFileName.replaceAll(FORMAT_REGEX, "_tmp" + REPLACE_REGEX);
            }
            fileOut = new FileOutputStream(fullFileName);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return void
     * @Author chen_bq
     * @Description 默认装饰表格
     * @Date 2019/10/18 17:06
     * @Param [sheet, row, column]
     */
    private static void defaultDecorationTable(XSSFWorkbook workbook, int row, int column, int maRow, int maxColumn) {
        XSSFSheet sheet = workbook.getSheet(DEFAULT_TABLE_FIRST_PAGE_NAME);
        if (AUTO_SIZE_COLUMN) {
            sheet.autoSizeColumn(column);
        }
        if (row == 0) {
            XSSFRow sheetRow = sheet.getRow(0);
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
            cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
            cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
            cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
            CellUtil.getCell(sheetRow, maxColumn).setCellStyle(cellStyle);
        }
    }

    private static void defaultDecorationTable(SXSSFWorkbook workbook, int row, int column, int maRow, int maxColumn) {
        SXSSFSheet sheet = workbook.getSheet(DEFAULT_TABLE_FIRST_PAGE_NAME);
        if (AUTO_SIZE_COLUMN) {
            // 高版本需要提前使用这个
            sheet.trackAllColumnsForAutoSizing();
            sheet.autoSizeColumn(column);
        }
//        SXSSFRow无法装饰
//        if (row == 0) {
//            SXSSFRow sheetRow = sheet.getRow(0);
//            CellStyle cellStyle = workbook.createCellStyle();
//            cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
//            cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
//            cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
//            cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
//            cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
//            CellUtil.getCell(sheetRow, maxColumn).setCellStyle(cellStyle);
//        }
    }

    /**
     * @return void
     * @Author chen_bq
     * @Description 构建工作表
     * @Date 2019/10/18 17:07
     * @Param [workbook, dataList]
     */
    public static void buildXSSFWorkbook(XSSFWorkbook workbook, List<List<String>> dataList) {
        XSSFSheet sheet = workbook.createSheet(DEFAULT_TABLE_FIRST_PAGE_NAME);
        buildTable(sheet, dataList);
        for (int i = 0; i < dataList.get(0).size(); i++) {
            defaultDecorationTable(workbook, i, i, dataList.size(), dataList.get(0).size());
        }
    }

    public static void buildXSSFWorkbook(SXSSFWorkbook workbook, List<List<String>> dataList) {
        SXSSFSheet sheet = workbook.createSheet(DEFAULT_TABLE_FIRST_PAGE_NAME);
        buildTable(sheet, dataList);
        for (int i = 0; i < dataList.get(0).size(); i++) {
            defaultDecorationTable(workbook, i, i, dataList.size(), dataList.get(0).size());
        }
    }

    /**
     * @return void
     * @Author chen_bq
     * @Description 构建表格数据
     * @Date 2019/10/18 17:07
     * @Param [sheet, dataList]
     */
    private static void buildTable(XSSFSheet sheet, List<List<String>> dataList) {
        if (sheet == null || dataList == null || dataList.size() <= 0) {
            return;
        }
        for (int i = 0; i < dataList.size(); i++) {
            XSSFRow row = sheet.createRow(i);
            buildRow(row, dataList.get(i));
        }
    }

    private static void buildTable(SXSSFSheet sheet, List<List<String>> dataList) {
        if (sheet == null || dataList == null || dataList.size() <= 0) {
            return;
        }
        for (int i = 0; i < dataList.size(); i++) {
            SXSSFRow row = sheet.createRow(i);
            buildRow(row, dataList.get(i));
        }
    }

    /**
     * @return void
     * @Author chen_bq
     * @Description 构建行数据
     * @Date 2019/10/18 17:07
     * @Param [row, tableList]
     */
    private static <T> void buildRow(XSSFRow row, List<T> tableList) {
        if (row == null || tableList == null || tableList.size() <= 0) {
            return;
        }
        for (int i = 0; i < tableList.size(); i++) {
            if (tableList.get(i) == null) {
                continue;
            }
            row.createCell(i).setCellValue(tableList.get(i).toString());
        }
    }

    private static <T> void buildRow(SXSSFRow row, List<T> tableList) {
        if (row == null || tableList == null || tableList.size() <= 0) {
            return;
        }
        for (int i = 0; i < tableList.size(); i++) {
            if (tableList.get(i) == null) {
                continue;
            }
            row.createCell(i).setCellValue(tableList.get(i).toString());
        }
    }

    public static void main(String[] args) {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                row.add(j + "ASD");
            }
            list.add(row);
        }
        String fullFileName = "E:\\work\\工作文档\\test";
        exportExcel(list, fullFileName);
    }


}
