package com.aprilhealth.demo;
/*
 *@author April0ne
 *@date 2019/11/20 16:58
 */


import com.aprilhealth.utils.WriteExcelPOIUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WriteExcelDemo {
    private boolean isExcel2003 = false;
    private List<String> headers = new ArrayList<>();
    private List<B01> dataList = new ArrayList();
    private String sheetName;

    @Test
    public void write() throws Exception{
        headers.add("日期");
        headers.add("可预约人数");
        headers.add("失败原因");
//        B01 b01 = new B01();
//        b01.setNumber("333");
//        b01.setOrderDate("2019/11/25");
//        b01.setMessage("历史时间不允许修改");
//        B01 b02 = new B01();
//        b02.setNumber("444");
//        b02.setOrderDate("2019/11/22");
//        b02.setMessage("不允许修改");
        B01 b01 = new B01();
        b01.setNumber(333);
        b01.setOrderDate(new Date());
        b01.setMessage("历史时间不允许修改");
        B01 b02 = new B01();
        b02.setNumber(444);
        b02.setOrderDate(new Date());
        b02.setMessage("不允许修改");
        dataList.add(b01);
        dataList.add(b02);
        sheetName = "批量导入失败原因";
        Workbook workbookFromObj = WriteExcelPOIUtils.getWorkbookFromObj(headers, dataList, isExcel2003, sheetName);

        workbookFromObj.write(new FileOutputStream("C:\\Users\\April\\Desktop\\20191120_test.xlsx"));

    }
}
