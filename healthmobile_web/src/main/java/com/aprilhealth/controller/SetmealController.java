package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/22 8:46
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.entity.Result;
import com.aprilhealth.pojo.Setmeal;
import com.aprilhealth.service.SetmealService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    /**
     * mobile : 查询所有套餐
     * @return
     */
    @PostMapping("/findAllSetmeal")
    public Result getSetmeal(){
        List<Setmeal> setmealList =  setmealService.findAllSetmeal();
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmealList);
    }

    /**
     * mobile : 查询套餐之ByID:查看套餐详情
     * @param id
     * @return
     */
    @PostMapping("/findById/{id}")
    public Result findById(@PathVariable(value = "id") Integer id){
        Result result = null;
        if (id != null && id != 0) {
            Setmeal setmeal = setmealService.findById(id);
            if (setmeal != null) {
                result = new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
            }else {
                result = new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
            }
        }else {
            result = new Result(false,MessageConstant.QUERY_SETMEAL_SUCCESS);
        }
        return result;
    }

}
