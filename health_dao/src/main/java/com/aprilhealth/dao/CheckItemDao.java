package com.aprilhealth.dao;
/*
 *@author April0ne
 *@date 2019/11/11 8:39
 *预约管理之检查项管理模块dao接口
 */


import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckItem;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckItemDao {
    /**
     * 新增检查项
     * @param checkItem 新增检查项的内容
     * @return 新增成功返回1 , 反之返回0
     */
    Integer addCheckItem(CheckItem checkItem);

    /**
     * 查詢(分頁查詢/條件查詢)
     * @param queryPageBean
     * @return
     */
    List<CheckItem> findPageCheckItem(QueryPageBean queryPageBean);

    /**
     * 查询单个项目
     * @param id 被查检查项id
     * @return 查询到的检查项对象
     */
    CheckItem findOneCheckItem(Integer id);

    /**
     * 查询表(t_checkgroup_checkitem)检查项信息(根据checkGroupId查询)
     * @param checkGroupId
     * @return
     */
    List<Integer> findCheckItemFromT_checkgroup_checkitem(Integer checkGroupId);

    /**
     * 查询检查项信息(所有检查项)
     * @return
     */
    List<CheckItem> findCheckItemFromT_checkitem();

    /**
     * 根据条件查询总项目检查数
     * @param queryPageBean
     * @return
     */
    long findSumCheckItem(QueryPageBean queryPageBean);


    /**
     * 修改检查项
     * @param checkItem 修改检查项的内容
     * @return 新增成功返回1 , 反之返回0
     */
    Integer updateCheckItem(CheckItem checkItem);


    /**
     * 删除单个项目
     * @param id 被删除检查项id
     * @return 删除成功返回1 , 反之返回0
     */
    Integer deleteCheckItem(Integer id);

    /**
     * 根据CheckItemID,从表t_checkgroup_checkitem查询对应关系
     * @param id CheckItemID
     * @return 结果>1 表示有关系,不能删除.
     */
    Integer findCheckGroup_CheckItemByCheckItemID(Integer id);

}
