package com.aprilhealth.utils;
/*
 *@author April0ne
 *@date 2019/11/20 16:23
 */

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WriteExcelPOIUtils {

    /**
     * 将数据写入Excel工作簿
     * @param header    表格的标题
     * @param dataList  所需写入的数据 List<Object>
     * @param isExcel2003   是否是excel2003还是更高的版本
     * @param sheetName     生成的excel中sheet的名字
     * @return  Workbook对象，之后直接写出即可，如workbook.write(new FileOutputStream("E://test/20190410_test.xlsx"));
     * @throws Exception
     */
    public static Workbook getWorkbookFromObj(List<String> header, List<?> dataList, boolean isExcel2003,
                                              String sheetName) throws IllegalAccessException,InvocationTargetException {
        Workbook wb;
        // 创建Workbook对象(excel的文档对象)
        if (isExcel2003) {
            wb = new HSSFWorkbook();
        } else {
            wb = new XSSFWorkbook();
        }
        // 建立新的sheet对象（excel的表单）
        Sheet sheet = wb.createSheet(sheetName);
        // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        int rowNum = 0;
        Row row0 = sheet.createRow(rowNum);
        if (!CollectionUtils.isEmpty(header)) {
            // 设置表头
            for (int i = 0; i < header.size(); i++) {
                Cell cell = row0.createCell(i);
                // 设置单元格样式
                cell.setCellStyle(ExcelStyle.getCellStyle(wb, "Calibri", (short) 12));
                sheet.setColumnWidth(i, 256 * 20);
                cell.setCellValue(header.get(i));
            }
            rowNum++;
        }
        if (!CollectionUtils.isEmpty(dataList)) {
            // 填充数据
            Class<? extends Object> objClass = dataList.get(0).getClass();
            Field[] fields = objClass.getDeclaredFields();
            for (int i = 0; i < dataList.size(); i++) {
                // 创建row对象
                Row row = sheet.createRow(rowNum);
                // 遍历获取每一个字段的值
                for (int j = 0; j < fields.length; j++) {
                    Object fieldVal = "";
                    Method[] methods = objClass.getDeclaredMethods();
                    for (Method method : methods) {
                        //获取单元格数据
                        if (method.getName().equalsIgnoreCase("get" + fields[j].getName())) {
//                            String property = (String) method.invoke(dataList.get(i), null);//在下面强转成string,此处不转.
                            Object property = method.invoke(dataList.get(i), null);
                            fieldVal = property == null ? "" : property;
                            break;
                        }
                    }
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(ExcelStyle.getCellStyle(wb, "Calibri", (short) 12));
                    if (CollectionUtils.isEmpty(header)) {
                        sheet.setColumnWidth(j, 256 * 20);
                    }
                    //单独处理日期字段数据
                    //设置单元格值
                    if (fields[j].getType() != Date.class){
                        cell.setCellValue(fieldVal.toString());//把所有单元格数据转换成字符串
                    }else {
                        //属性类型是date
                        Date fieldValDate = (Date) fieldVal;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        String formatDate = simpleDateFormat.format(fieldValDate);
                        cell.setCellValue(formatDate);
                    }
                }
                rowNum++;
            }
        }
        return wb;
    }

//    public static boolean validateExcel(String filePath) {
//        /** 检查文件名是否为空或者是否是Excel格式的文件 */
//        if (filePath == null
//                || !(isExcel2003(filePath) || isExcel2007(filePath))) {
//            // "文件名不是excel格式";
//            return false;
//        }
//        /** 检查文件是否存在 */
//        File file = new File(filePath);
//        if (file == null || !file.exists()) {
//            // "文件不存在";
//            return false;
//        }
//        return true;
//    }
//
//    public static boolean isExcel2003(String filePath) {
//        return filePath.matches("^.+\\.(?i)(xls)$");
//    }
//
//    public static boolean isExcel2007(String filePath) {
//        return filePath.matches("^.+\\.(?i)(xlsx)$");
//    }

}
