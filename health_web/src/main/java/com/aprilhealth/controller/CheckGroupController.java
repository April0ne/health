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
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.CheckItem;
import com.aprilhealth.service.CheckGroupService;
import com.aprilhealth.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;


    /**
     * 分页查询
     * @return
     * @throws Exception
     */
    @PostMapping("/findPageCheckGroup")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result findPageCheckGroup(@RequestBody QueryPageBean queryPageBean){
        Result result = null;
        PageResult pageCheckGroup = checkGroupService.findPageCheckGroup(queryPageBean);
        result = new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageCheckGroup);
        return result;
    }

    /**
     * 新增检查组(必须要有两个参数,这是前段封装数据限制的 ---- 本来只用了一个参数)
     * @param checkGroup
     * @param checkItemId
     * @return
     * @throws Exception
     */
    @PostMapping("/addCheckGroup")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_ADD')")
    public Result addCheckGroup(@RequestBody CheckGroup checkGroup,Integer[] checkItemId){
        Result result = null;
        boolean b = checkGroupService.addCheckGroup(checkGroup,checkItemId);
        if (b) {
            result = new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
        }else {
            result = new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return result;
    }

    /**
     * 通过id查询
     * @return
     * @throws Exception
     */
    @GetMapping("/findOneCheckGroup/{id}")
    public Result findOneCheckGroup(@PathVariable(value = "id") Integer id) {
        Result result = null;
        CheckGroup oneCheckGroup = checkGroupService.findOneCheckGroup(id);
        result = new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,oneCheckGroup);
        return result;
    }

    /**
     * 查询所有检查项信息(新增) ---- 复用checkItem的
     * @return
     * @throws Exception
     */
    @GetMapping("/findCheckItemFromT_checkitem")
    public Result findCheckItemFromT_checkitem(){
        Result result = null;
        List<CheckItem> checkItemList = checkGroupService.findCheckItemFromT_checkitem();
        result = new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        return result;
    }

    /**
     * 查询检查项,表(t_checkgroup_checkitem)检查项信息(根据checkGroupId查询)
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/findCheckItemIdFromT_checkgroup_checkitem/{id}")
    public Result findCheckItemIdFromT_checkgroup_checkitem(@PathVariable(value = "id") Integer id){
        Result result = null;
        List<Integer> checkItemIdList = checkGroupService.findCheckItemIdFromT_checkgroup_checkitem(id);
        result = new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIdList);
        return result;
    }

    /**
     * 更新检查组
     * @param checkGroup
     * @param checkItemId
     * @return
     */
    @PostMapping("/updateCheckGroup")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_EDIT')")
    public Result updateCheckGroup(@RequestBody CheckGroup checkGroup,Integer[] checkItemId) {
        Result result = null;
        boolean b = checkGroupService.updateCheckGroup(checkGroup,checkItemId);
        if (b) {
            result = new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }else {
            result = new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }

        return result;
    }

    /**
     * 删除检查组
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/deleteFromCheckGroup/{id}")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_DELETE')")
    public Result deleteFromCheckGroup(@PathVariable(value = "id") Integer id){
        Result result = null;
//        boolean b = checkGroupService.deleteFromCheckGroup(id);
//        if (b) {
//            result = new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
//        }else {
//            result = new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
//        }

        try {
            checkGroupService.deleteFromCheckGroup(id);
            result = new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            result = new Result(false,e.getMessage());
        }

        return result;
    }


}
