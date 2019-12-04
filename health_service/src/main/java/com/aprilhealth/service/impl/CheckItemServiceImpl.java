package com.aprilhealth.service.impl;
/*
 *@author April0ne
 *@date 2019/11/11 9:00
 *预约管理之检查项管理模块service接口的实现类
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.dao.CheckItemDao;
import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckItem;
import com.aprilhealth.service.CheckItemService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service(protocol = "dubbo",interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService{

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增检查项
     * @param checkItem 新增检查项的内容
     * @return
     */
    @Override
    public boolean addCheckItem (CheckItem checkItem) throws Exception {
        return checkItemDao.addCheckItem(checkItem) > 0;
    }

    /**
     * 分页查询检查项目
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPageCheckItem(QueryPageBean queryPageBean) throws Exception {
        //使用使用PageHalper插件完成分页查询(略)
        //重新封装当前页码的值
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        currentPage = (currentPage - 1) * pageSize;
        queryPageBean.setCurrentPage(currentPage);
        //当前页检查项数据
        List<CheckItem> pageCheckItem = checkItemDao.findPageCheckItem(queryPageBean);
        //当前条件总检查项数
        long sumCheckItem = checkItemDao.findSumCheckItem(queryPageBean);
//        System.out.println("无条件总检查项数=" + sumCheckItem);
//        //总页数
//        long totalPage = sumCheckItem;
//        if (sumCheckItem > 0) {
//            if (sumCheckItem % pageSize != 0) {
//                totalPage = sumCheckItem / pageSize + 1;
//            }else {
//                totalPage = sumCheckItem / pageSize;
//            }
//        }

        //封装分页查询总结果
        PageResult pageResult = new PageResult(sumCheckItem,pageCheckItem);

        return pageResult;
    }

    /**
     * 更新检查项
     * @param checkItem 修改检查项的内容
     * @return
     */
    @Override
    public boolean updateCheckItem(CheckItem checkItem) throws Exception {
        return checkItemDao.updateCheckItem(checkItem) > 0;
    }

    /**
     * 查询单个检查项目
     * @param id 被查检查项id
     * @return
     */
    @Override
    public CheckItem findOneCheckItem(Integer id) throws Exception {
        return checkItemDao.findOneCheckItem(id);
    }

    /**
     * 删除检查项目
     * @param id 被删除检查项id
     * @return
     */
    @Override
    public boolean deleteCheckItem(Integer id) throws Exception  {
        //判断当前项目是否和checkgroup存在关联关系
        boolean flag;
        Integer cg_ci = checkItemDao.findCheckGroup_CheckItemByCheckItemID(id);
        if (cg_ci > 0) {
            flag = false;
        }else {
            flag = checkItemDao.deleteCheckItem(id) > 0 ? true:false;
        }
        return flag;
    }
}
