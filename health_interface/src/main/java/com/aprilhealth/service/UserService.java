package com.aprilhealth.service;
/*
 *@author April0ne
 *@date 2019/11/24 19:55
 */


import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.Role;
import com.aprilhealth.pojo.User;

import java.util.List;

public interface UserService {
    /**
     * 查询用户: 根据用户名
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPageUser(QueryPageBean queryPageBean);

    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAllRoles();

    /**
     * 新增员工
     * @param user
     * @param rolesId
     * @return
     */
    boolean addUser(User user, Integer[] rolesId);
}
