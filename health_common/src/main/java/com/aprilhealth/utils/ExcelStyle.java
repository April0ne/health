package com.aprilhealth.utils;
/*
 *@author April0ne
 *@date 2019/11/20 16:28
 */


import org.apache.poi.ss.usermodel.*;

public class ExcelStyle {
    /**
     * 设置单元格的边框（细）且为黑色，字体水平垂直居中，自动换行
     * @param workbook
     * @param fontName
     * @param fontSize
     * @return
     */
    public static CellStyle getCellStyle(Workbook workbook, String fontName, short fontSize){
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        // 设置上下左右四个边框宽度
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
        // 设置上下左右四个边框颜色
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        // 水平居中，垂直居中，自动换行
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(false);
        // 设置字体样式及大小
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);

        style.setFont(font);

        return style;
    }
}
