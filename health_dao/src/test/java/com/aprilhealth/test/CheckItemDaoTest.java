package com.aprilhealth.test;
/*
 *@author April0ne
 *@date 2019/11/11 8:57
 */


import com.aprilhealth.dao.CheckItemDao;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-dao.xml")
public class CheckItemDaoTest {

    @Autowired
    private CheckItemDao checkItemDao;

    @Test
    public void add(){
        checkItemDao.addCheckItem(new CheckItem());
    }

    @Test
    public void findPageCheckItem(){
        QueryPageBean queryPageBean = new QueryPageBean(1,10,"");
        List<CheckItem> page = checkItemDao.findPageCheckItem(queryPageBean);
        for (CheckItem checkItem : page) {
            System.out.println(checkItem);
        }
    }

    @Test
    public void updateCheckItem(){
        CheckItem checkItem = new CheckItem();
        checkItem.setAge("99");
        checkItem.setId(28);
        checkItemDao.updateCheckItem(checkItem);
    }

    @Test
    public void findOneCheckItem(){
        CheckItem oneCheckItem = checkItemDao.findOneCheckItem(28);
        System.out.println(oneCheckItem);
    }

    @Test
    public void deleteCheckItem(){
        int i = checkItemDao.deleteCheckItem(28);
        System.out.println(i);
    }

    @Test
    public void findCheckGroup_CheckItemByCheckItemID(){
        Integer checkGroup_checkItemByCheckItemID = checkItemDao.findCheckGroup_CheckItemByCheckItemID(28);
        System.out.println(checkGroup_checkItemByCheckItemID);
    }

    @Test
    public void findSumCheckItem(){
        QueryPageBean queryPageBean = new QueryPageBean(1,2,"");
        long sumPageCheckItem = checkItemDao.findSumCheckItem(queryPageBean);
        System.out.println(sumPageCheckItem);
    }

}
