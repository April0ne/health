package com.aprilhealth.test;
/*
 *@author April0ne
 *@date 2019/11/11 9:07
 */


import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.CheckItem;
import com.aprilhealth.service.CheckGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext*.xml")
public class CheckGroupServiceTest {

    @Autowired
    private CheckGroupService checkGroupService;

    /**
     * 分页查询(使用插件)
     */
    @Test
    public void findPageCheckGroup() {
        QueryPageBean queryPageBean = new QueryPageBean(1,2,"");
        PageResult pageCheckGroup = checkGroupService.findPageCheckGroup(queryPageBean);
        System.out.println("总页数"+pageCheckGroup.getTotal());
        List rows = pageCheckGroup.getRows();
        for (Object row : rows) {
            System.out.println("checkGroup="+row);
        }
    }


    /**
     * 查询单个
     */
    @Test
    public void findOneCheckGroup(){
        System.out.println(checkGroupService.findOneCheckGroup(5));
    }

    /**
     * 新增检查组
     */
    @Test
    public void addCheckGroup(){
        Integer[] i  = new Integer[3];
        i[0] = 76;
        i[1] = 77;
        i[2] = 78;
        CheckGroup checkGroup = new CheckGroup();
        checkGroup.setCode("19");
        boolean b = checkGroupService.addCheckGroup(checkGroup,i);
        System.out.println(b);
    }

    /**
     * 删除检查组
     */
    @Test
    public void deleteFromCheckGroup(){
        System.out.println("删除结果 = " + checkGroupService.deleteFromCheckGroup(28));
    }

    /**
     * 更新检查组
     */
    @Test
    public void updateCheckGroup(){
        CheckGroup checkGroup = new CheckGroup();
        checkGroup.setId(60);
        Integer[] i  = new Integer[3];
        i[0] = 76;
        i[1] = 77;
        i[2] = 78;
        boolean b = checkGroupService.updateCheckGroup(checkGroup,i);
        System.out.println("更新结果 = " + b);

    }

}
