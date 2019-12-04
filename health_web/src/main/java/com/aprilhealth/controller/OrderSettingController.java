package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/11 9:17
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.constant.OrderDateConstant;
import com.aprilhealth.entity.ExcelModuleOrderSettingFailed;
import com.aprilhealth.entity.Result;
import com.aprilhealth.pojo.OrderSetting;
import com.aprilhealth.service.OrderSettingService;
import com.aprilhealth.utils.ReadExcelPOIUtils;
import com.aprilhealth.utils.WriteExcelPOIUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 查询预约设置信息
     * @param date
     * @return
     * @throws Exception
     */
    @PostMapping("/findMonthOrderSetting")
    @PreAuthorize("hasAnyAuthority('ORDERSETTING')")
    public Result findMonthOrderSetting(String date) throws RuntimeException{
        Result result = null;
        if (date != null || !"".equals(date)) {
            List<Map> monthOrderSetting = orderSettingService.findMonthOrderSetting(date);
            result  = new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,monthOrderSetting);
        }else {
            result  = new Result(true,MessageConstant.QUERY_ORDER_FAIL);
        }
        return result;
    }

    /**
     * 编辑预约设置信息(新增/修改)
     * @return
     * @throws Exception
     */
    @PostMapping("/editOrderSetting")
    @PreAuthorize("hasAnyAuthority('ORDERSETTING')")
    public Result editOrderSetting(@RequestBody OrderSetting orderSetting)throws RuntimeException{
        Result result = null;
        if (orderSetting != null) {
            try {
                if (orderSettingService.editOrderSetting(orderSetting)) {
                    result = new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
                }else {
                    result = new Result(false,MessageConstant.ORDERSETTING_FAIL);
                }
            } catch (Exception e) {
                result = new Result(false,e.getMessage());
            }
        }
        return result;
    }


    /**
     * 批量新增
     * @return
     * @throws Exception
     */
    @RequestMapping("/batchAdd")
    @PreAuthorize("hasAnyAuthority('ORDERSETTING')")
    public Result batchAddOrderSetting(@RequestParam("excelFile") MultipartFile excelFile)throws RuntimeException{//excelFile 指前端批量上传name属性的值
        boolean isDownloadExcel = false;
        try {
            //读取excel中的数据,写入数据库
            List<String[]> strings = ReadExcelPOIUtils.readExcel(excelFile);
            if (strings != null && strings.size() > 0) {
                List<OrderSetting> orderSettings = new ArrayList<>();
                for (String[] string : strings) {
                    OrderSetting orderSetting = new OrderSetting(new Date(string[0]), Integer.parseInt(string[1]));
                    orderSettings.add(orderSetting);
                }
                orderSettingService.addOrderSetting(orderSettings);
                //为false,意味着所有数据写入成功,不需要去下载excel表
                if (orderSettingService.setExcelModule()) {
                    //返回,并告知,无需下载excel
                    return new Result(true,MessageConstant.ORDERSETTING_SUCCESS,false);

                }
                return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return null;
    }

    /**
     * 下载excel表,内容为:批量上传时,为上传成功的内容.
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/download")
    public Result downloadOrderSettingFailed(HttpServletRequest request,HttpServletResponse response)throws RuntimeException{
        ServletOutputStream out = null;
        Workbook workbookFromObj = null;
        // 去redis下载数据,并写出至excel表.
        ExcelModuleOrderSettingFailed params = orderSettingService.getOrderSettingFailed(OrderDateConstant.REDIS_KEY_SAVE_EXCEL);
        try {
            workbookFromObj = WriteExcelPOIUtils.getWorkbookFromObj(params.getHeader(), params.getData(), params.isExcel2003(), params.getSheetName());
            //通过输出流进行文件下载==已经response,不能再return result;
            out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=fail.xlsx");
            workbookFromObj.write(out);
            out.flush();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (workbookFromObj != null) {
                try {
                    workbookFromObj.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return null;
    }


}
