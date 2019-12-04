package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/11 9:17
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.entity.Result;
import com.aprilhealth.pojo.CheckItem;
import com.aprilhealth.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/checkItem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;


    /**
     * 添加
     * @param checkItem
     * @return
     */
    @PostMapping("/addCheckItem")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result addCheckItem(@RequestBody CheckItem checkItem) throws Exception{
        Result result = null;
        boolean add = checkItemService.addCheckItem(checkItem);
        if (add){
            result = new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
        }else {
            result = new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return result;
    }

    /**
     * 分页查询
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/findPageCheckItem")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findPageCheckItem(@RequestBody QueryPageBean queryPageBean) throws Exception{
        PageResult pageCheckItem = checkItemService.findPageCheckItem(queryPageBean);
        Result result  = new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,pageCheckItem);
        return result;
    }

    /**
     * 更新数据
     * @param checkItem
     * @return
     * @throws Exception
     */
    @PostMapping("/updateCheckItem")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result updateCheckItem(@RequestBody CheckItem checkItem) throws Exception{
        Result result = null;
        boolean flag = checkItemService.updateCheckItem(checkItem);
        if (flag){
            result = new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        }else {
            result = new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return result;
    }

    /**
     * 查询单个用户(编辑时用)
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/findOneCheckItem/{id}")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")//前端访问地址:axios.post("/checkItem/findOneCheckItem/"+id+".do",如果控制器不是*.do,前端访问就是:axios.post("/checkItem/findOneCheckItem/id="+id,
    public Result findOneCheckItem(@PathVariable(value = "id") Integer id) throws Exception{
        CheckItem oneCheckItem = checkItemService.findOneCheckItem(id);
        Result result = new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,oneCheckItem);
        return result;
    }

    /**
     * 删除检查项
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteCheckItem/{id}")
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    public Result deleteCheckItem(@PathVariable("id") Integer id) throws Exception{
        Result result = null;
        boolean b = checkItemService.deleteCheckItem(id);
        if (b) {
            result = new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        }else {
            result = new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return result;
    }

}
