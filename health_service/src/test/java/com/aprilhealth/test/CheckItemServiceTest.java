package com.aprilhealth.test;
/*
 *@author April0ne
 *@date 2019/11/11 9:07
 */


import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckItem;
import com.aprilhealth.service.CheckItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext*.xml")
public class CheckItemServiceTest {

    @Autowired
    private CheckItemService checkItemService;

    @Test
    public void addCheckItem() throws Exception{
        CheckItem checkItem = new CheckItem();
        checkItem.setAge("11");

        boolean add = checkItemService.addCheckItem(checkItem);
        System.out.println(add);
    }

    @Test
    public void findPageCheckItem()throws Exception{
        QueryPageBean queryPageBean = new QueryPageBean();
        queryPageBean.setCurrentPage(1);
        queryPageBean.setQueryString("");
        queryPageBean.setPageSize(10);
        PageResult pageCheckItem = checkItemService.findPageCheckItem(queryPageBean);
        Long total = pageCheckItem.getTotal();
        System.out.println("总页数"+total);
        List rows = pageCheckItem.getRows();
        System.out.println("当前页总数" + rows.size());
        for (Object row : rows) {
            System.out.println("分页list结果=" + row);
        }
    }

    @Test
    public void deleteCheckItem()throws Exception{
        System.out.println(checkItemService.deleteCheckItem(28));
    }
}
