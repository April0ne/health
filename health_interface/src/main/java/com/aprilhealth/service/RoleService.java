package com.aprilhealth.service;
/*
 *@author April0ne
 *@date 2019/11/27 12:53
 */


import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.Permission;
import com.aprilhealth.pojo.Role;

import java.util.List;

public interface RoleService {
    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findPageRole(QueryPageBean queryPageBean);

    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAllPermission();

    /**
     * 新增角色
     * @param role
     * @param permissionsId
     * @return
     */
    boolean addRole(Role role, Integer[] permissionsId);

    /**
     * 查询角色(更新之回显数据)
     * @param roleId
     * @return
     */
    Role findRoleById(Integer roleId);

    /**
     * 查询角色已有权限
     * @param roleId
     * @return
     */
    List<Integer> findRoleHadPermission(Integer roleId);

    /**
     * 更新角色
     * @param role
     * @param permissionsId
     * @return
     */
    void updateRole(Role role, Integer[] permissionsId);

    /**
     * 查询角色是否有用户(在职)使用.
     * @param roleId
     * @return
     */
    boolean roleHadUserUse(Integer roleId);

    /**
     * 删除角色
     * @param roleId
     */
    void deleteRole(Integer roleId);
}
