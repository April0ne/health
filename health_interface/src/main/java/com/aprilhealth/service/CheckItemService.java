package com.aprilhealth.service;
/*
 *@author April0ne
 *@date 2019/11/11 8:36
 *预约管理之检查项管理模块Service接口
 */


import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CheckItemService {

    /**
     * 新增检查项
     * @param checkItem 新增检查项的内容
     * @return 新增成功返回1 , 反之返回0
     */
    boolean addCheckItem  (CheckItem checkItem) throws Exception;


    /**
     * 查詢(分頁查詢/條件查詢)
     * @param queryPageBean
     * @return
     */
    PageResult findPageCheckItem (QueryPageBean queryPageBean) throws Exception;


    /**
     * 修改检查项
     * @param checkItem 修改检查项的内容
     * @return 新增成功返回1 , 反之返回0
     */
    boolean updateCheckItem(CheckItem checkItem) throws Exception;

    /**
     * 查询单个项目
     * @param id 被查检查项id
     * @return 查询到的检查项对象
     */
    CheckItem findOneCheckItem(Integer id) throws Exception;


    /**
     * 删除单个项目
     * @param id 被删除检查项id
     * @return 删除成功返回1 , 反之返回0
     */
    boolean deleteCheckItem(Integer id) throws Exception;

}
