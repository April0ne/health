package com.aprilhealth.entity;
/*
 *@author April0ne
 *@date 2019/11/20 18:13
 *创建到处数据excel模型
 */


import java.io.Serializable;
import java.util.List;

public class ExcelModuleOrderSettingFailed implements Serializable{
    private List<String> header;
    private List<?> data;
    private boolean isExcel2003;
    private String sheetName;

    public ExcelModuleOrderSettingFailed(){}

    public ExcelModuleOrderSettingFailed(List<String> header, List data, boolean isExcel2003, String sheetName) {
        this.header = header;
        this.data = data;
        this.isExcel2003 = isExcel2003;
        this.sheetName = sheetName;
    }

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public boolean isExcel2003() {
        return isExcel2003;
    }

    public void setExcel2003(boolean excel2003) {
        this.isExcel2003 = excel2003;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public String toString() {
        return "ExcelModuleOrderSettingFailed{" +
                "header=" + header +
                ", data=" + data +
                ", isExcel2003=" + isExcel2003 +
                ", sheetName='" + sheetName + '\'' +
                '}';
    }
}
