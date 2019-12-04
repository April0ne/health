package com.aprilhealth.service;
/*
 *@author April0ne
 *@date 2019/11/14 20:44
 *处理检查组业务的接口
 */


import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.CheckItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface CheckGroupService {

    /**
     * 查询byId
     * @param id CheckGroup表id
     * @return
     */
    CheckGroup findOneCheckGroup(Integer id) ;

    /**
     * 查询检查项信息(所有检查项)
     * @return
     */
    List<CheckItem>  findCheckItemFromT_checkitem();

    /**
     * 查询表(t_checkgroup_checkitem)检查项信息(根据checkGroupId查询)
     * @param checkGroupId
     * @return
     */
    List<Integer> findCheckItemIdFromT_checkgroup_checkitem(Integer checkGroupId);

    /**
     * 分页查询(使用分页插件)
     * @param queryPageBean 封装了分页查询相关的信息,由前端传递
     * @return
     */
    PageResult findPageCheckGroup(QueryPageBean queryPageBean);


    /**
     * 更新检查组数据
     * @param checkGroup
     * @param checkItemId
     * @return
     */
    boolean updateCheckGroup(CheckGroup checkGroup,Integer[] checkItemId);

    /**
     * 新增数据,往t_checkGroup
     * @param checkGroup
     * @param checkItemId
     * @return 返回值大于0,boolean = true
     */
    boolean addCheckGroup(CheckGroup checkGroup,Integer[] checkItemId);


    /**
     * 新增数据,往t_checkgroup_checkitem表中
     * @param checkGroup
     * @param checkItemId
     */
    void addToT_checkgroup_checkitem(CheckGroup checkGroup,Integer[] checkItemId);


    /**
     * 删除t_checkGroup记录
     * @param id
     * @return 返回值大于0,boolean = true
     */
    boolean deleteFromCheckGroup(Integer id);

    /**
     * 删除关联表 ,根据checkgroup_id删除表t_checkgroup_checkitem 数据
     * @param checkGroupId
     * @return
     */
    boolean deleteFromT_checkgroup_checkitem(Integer checkGroupId);




}
