package com.aprilhealth.test;
/*
 *@author April0ne
 *@date 2019/11/11 9:07
 */


import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.Setmeal;
import com.aprilhealth.service.CheckGroupService;
import com.aprilhealth.service.SetmealService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext*.xml")
public class SetmealServiceTest {

    @Autowired
    private SetmealService setmealService;

    /**
     * 分页查询(使用插件)
     */
    @Test
    public void findPageFromSetmeal() {
        QueryPageBean queryPageBean = new QueryPageBean(1,1,"");
        PageResult pageFromSetmeal = setmealService.findPageFromSetmeal(queryPageBean);
        System.out.println("套餐总数 = " + pageFromSetmeal.getTotal());
        List rows = pageFromSetmeal.getRows();
        System.out.println("套餐一页展示数量 = " +rows.size());
        for (Object row : rows) {
            System.out.println("套餐一页展示数据 = " + row);
        }
    }

    @Test
    public void addSetmeal(){
        Setmeal setmeal = new Setmeal();
        setmeal.setCode("14");
        Integer[] checkgroupIds = new Integer[3];//里面有3null,主键不为null,所以报错.
        checkgroupIds[0] = 51;
        checkgroupIds[2] = 52;
        boolean b = setmealService.addSetmeal(setmeal, checkgroupIds);
        System.out.println(b);
    }




}
