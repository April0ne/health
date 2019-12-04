package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/26 13:52
 *运营数据
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.entity.Result;
import com.aprilhealth.service.ReportService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private ReportService reportService;

    /**
     * 查询运营数据
     * @return
     * @throws Exception
     */
    @PostMapping("/getBusinessReportData")
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result getBusinessReportData() throws Exception{
        Result result = null;
        try {
            Map<String,Object> mapResult = reportService.getBusinessData();
            result = new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,mapResult);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return result;
    }

    /**
     * 导出运营数据
     * @return
     * @throws Exception
     */
    @GetMapping("/exportBusinessReport")
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try{
            //远程调用报表服务获取报表数据
            Map<String, Object> result = reportService.getBusinessData();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Long todayNewMember = (Long) result.get("todayNewMember");
            Long totalMember = (Long) result.get("totalMember");
            Long thisWeekNewMember = (Long) result.get("thisWeekNewMember");
            Long thisMonthNewMember = (Long) result.get("thisMonthNewMember");
            Long todayOrderNumber = (Long) result.get("todayOrderNumber");
            Long thisWeekOrderNumber = (Long) result.get("thisWeekOrderNumber");
            Long thisMonthOrderNumber = (Long) result.get("thisMonthOrderNumber");
            Long todayVisitsNumber = (Long) result.get("todayVisitsNumber");
            Long thisWeekVisitsNumber = (Long) result.get("thisWeekVisitsNumber");
            Long thisMonthVisitsNumber = (Long) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获得Excel模板文件绝对路径
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template") +
                    File.separator + "report_template.xlsx";

            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

            return null;
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }
    }
}
