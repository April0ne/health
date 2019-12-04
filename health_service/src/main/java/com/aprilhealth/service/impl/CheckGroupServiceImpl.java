package com.aprilhealth.service.impl;
/*
 *@author April0ne
 *@date 2019/11/14 20:43
 *处理检查组业务的接口的实现类
 */


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.dao.CheckGroupDao;
import com.aprilhealth.dao.CheckItemDao;
import com.aprilhealth.dao.PermissionDao;
import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.CheckItem;
import com.aprilhealth.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service(protocol = "dubbo",interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService{

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 查询byId
     * @param checkGroupId CheckGroup表id
     * @return
     */
    @Override
    public CheckGroup findOneCheckGroup(Integer checkGroupId)  {
        return checkGroupDao.findOneCheckGroup(checkGroupId);
    }


    /**
     * 查询检查项信息(新增)
     * @return
     */
    @Override
    public List<CheckItem>  findCheckItemFromT_checkitem(){
        //封装检查项数据
        List<CheckItem> checkItemList = checkItemDao.findCheckItemFromT_checkitem();
        return  checkItemList;
    }

    /**
     * 查询表(t_checkgroup_checkitem)检查项信息(根据checkGroupId查询) (更新)
     * @param checkGroupId
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdFromT_checkgroup_checkitem(Integer checkGroupId) {
        return checkItemDao.findCheckItemFromT_checkgroup_checkitem(checkGroupId);
    }

    /**
     * 分页查询(使用分页插件)
     * @param queryPageBean 封装了分页查询相关的信息,由前端传递
     * @return
     */
    @Override
    public PageResult findPageCheckGroup(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> pageCheckGroup = checkGroupDao.findPageCheckGroup(queryString);
        return new PageResult(pageCheckGroup.getTotal(),pageCheckGroup.getResult());
    }


    /**
     * 新增数据,往t_checkGroup
     * @param checkGroup
     * * @param checkItemId
     * @return
     */
    @Override
    public boolean addCheckGroup(CheckGroup checkGroup,Integer[] checkItemId) {
        Integer addCG = checkGroupDao.addCheckGroup(checkGroup);
        if (addCG > 0 && addCG != null) {
            addToT_checkgroup_checkitem(checkGroup,checkItemId);
//            int i = 1/0;
            return true;
        }
        return false;
    }

    /**
     * 新增数据,往t_checkgroup_checkitem表中
     * @param checkGroup
     * @param checkItemId
     * @return
     */
    @Override
    public void addToT_checkgroup_checkitem(CheckGroup checkGroup,Integer[] checkItemId) {
        Integer checkGroup_id = checkGroup.getId();
        if (checkItemId != null && checkItemId.length > 0) {
            for (Integer checkitem_id : checkItemId) {
                Map<String,Integer> map = new HashMap<String,Integer>();
                map.put("checkgroup_id",checkGroup_id);
                map.put("checkitem_id",checkitem_id);
                checkGroupDao.addToT_checkgroup_checkitem(map);
            }
        }
    }

    /**
     * 更新检查组
     * @param checkGroup
     * @return
     */
    @Override
    public boolean updateCheckGroup(CheckGroup checkGroup,Integer[] checkItemId) {
        Integer updateCheckGroup = checkGroupDao.updateCheckGroup(checkGroup);
        if (updateCheckGroup > 0) {
            //删除中间表相关数据
            if ( deleteFromT_checkgroup_checkitem(checkGroup.getId()) ) {
                // int i = 1/0;
                //往中间表重新插入相关数据
                addToT_checkgroup_checkitem(checkGroup,checkItemId);
                return true;
            }
        }
        return false;
    }


    /**
     * 删除t_checkGroup记录
     * @param checkGroupId
     * @return
     */
    @Override
    public boolean deleteFromCheckGroup(Integer checkGroupId) {
        long count = checkGroupDao.findCountByT_setmeal_checkgroup(checkGroupId);
        if (count > 0) {
             throw new RuntimeException("当前检查组被套餐引用，不能删除");
//            return false;
        }

//        int i = 1/0;

        //删除关联表数据
        if (deleteFromT_checkgroup_checkitem(checkGroupId)) {
            //删除主表数据
            return checkGroupDao.deleteFromCheckGroup(checkGroupId) > 0 ? true:false;
        }


        return false;
    }

    /**
     * 删除关联表 --t_checkgroup_checkitem 数据,根据checkgroup_id
     * @param checkGroupId
     * @return
     */
    @Override
    public boolean deleteFromT_checkgroup_checkitem(Integer checkGroupId) {
        //先查询是否有关联数据,如果有就删除,如果没有就不删除,而是直接去删除主表数据即可
        List<Integer> checkItemIdList = findCheckItemIdFromT_checkgroup_checkitem(checkGroupId);
        if (checkItemIdList != null && checkItemIdList.size() > 0) {
            if(checkGroupDao.deleteFromT_checkgroup_checkitem(checkGroupId) > 0){
                return true;
            }else {
                return false;
            }
        }
        return true;
    }
}
