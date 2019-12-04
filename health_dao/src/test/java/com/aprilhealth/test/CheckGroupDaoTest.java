package com.aprilhealth.test;
/*
 *@author April0ne
 *@date 2019/11/11 8:57
 */


import com.aprilhealth.dao.CheckGroupDao;
import com.aprilhealth.dao.CheckItemDao;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.CheckItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-dao.xml")
public class CheckGroupDaoTest {

    @Autowired
    private CheckGroupDao checkGroupDao;

    //注解动态sql test
    @Test
    public void test(){
        List<CheckGroup> pageCheckGroup = checkGroupDao.test();
        for (CheckGroup checkGroup : pageCheckGroup) {
            System.out.println(checkGroup);
        }
    }

    /**
     * 分页查询测试
     */
    @Test
    public void findPageCheckGroup(){
//        PageHelper.startPage(1,2);//为什么有这一行就报错了?
        Page<CheckGroup> normal = checkGroupDao.findPageCheckGroup("");
        List<CheckGroup> result = normal.getResult();
        long total = normal.getTotal();
        System.out.println("总页数"+total);
        for (CheckGroup checkGroup : result) {
            System.out.println("检查组"+checkGroup);
        }

    }

    /**
     * 查询单个
     */
    @Test
    public void findOneCheckGroup(){
        CheckGroup oneCheckGroup = checkGroupDao.findOneCheckGroup(5);
        System.out.println(oneCheckGroup);
    }

    /**
     * 新增
     */
    @Test
    public void addCheckGroup(){
        CheckGroup checkGroup = new CheckGroup();
        checkGroup.setCode("1111");
        Integer integer = checkGroupDao.addCheckGroup(checkGroup);
        System.out.println(integer);
    }

    /**
     * 删除之t_checkgroup表记录
     */
    @Test
    public void deleteCheckGroup(){
        Integer integer = checkGroupDao.deleteFromCheckGroup(16);
        System.out.println(integer);
    }

    /**
     * 删除之前查询checkgroup与setmeal的关系表,如果有数据不允许删除
     */
    @Test
    public void findCountByT_setmeal_checkgroup(){
        long countByT_setmeal_checkgroup = checkGroupDao.findCountByT_setmeal_checkgroup(10);
        System.out.println(countByT_setmeal_checkgroup);
    }

    /**
     * 删除关联表数据
     */
    @Test
    public void deleteFromT_checkitem_checkgroup(){
        Integer integer = checkGroupDao.deleteFromT_checkgroup_checkitem(17);
        System.out.println(integer);

    }

}
