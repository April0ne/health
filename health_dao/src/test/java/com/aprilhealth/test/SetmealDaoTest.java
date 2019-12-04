package com.aprilhealth.test;
/*
 *@author April0ne
 *@date 2019/11/11 8:57
 */


import com.aprilhealth.dao.CheckGroupDao;
import com.aprilhealth.dao.SetmealDao;
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-dao.xml")
public class SetmealDaoTest {

    @Autowired
    private SetmealDao setmealDao;

    @Test
    public void findById(){
        Integer id = 12;
        Setmeal byId = setmealDao.findById(12);
        System.out.println("Setmeal=" + byId);
        List<CheckGroup> checkGroups = byId.getCheckGroups();
        for (CheckGroup checkGroup : checkGroups) {
            System.out.println("checkGroup=" + checkGroup);
        }
    }


}
