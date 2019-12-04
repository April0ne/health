package com.aprilhealth.dao;
/*
 *@author April0ne
 *@date 2019/11/24 20:45
 */


import com.aprilhealth.pojo.Menu;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashSet;
import java.util.List;

public interface MenuDao {
//    /**
//     * 查询menu_id集合: 从中间表 t_role_menu
//     * @param role_id
//     * @return
//     */
//    @Select("select menu_id from t_role_menu  where role_id = #{role_id}")
//    List<Integer> findMenuIdByRole(Integer role_id);
//
//    /**
//     * 查询menu: 根据menuId
//     * @param menuId
//     * @return
//     */
//    @Select("select * from t_menu where id = #{id}")
//    Menu findMenuById(Integer menuId);


    //整合上述两个sql
    @Select("select tm.* from t_role_menu trm , t_menu tm where trm.role_id = #{role_id} and tm.id = trm.menu_id")
    LinkedHashSet<Menu> findMenuIdByRoleId(Integer role_id);
}
