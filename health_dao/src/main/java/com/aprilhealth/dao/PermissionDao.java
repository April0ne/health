package com.aprilhealth.dao;
/*
 *@author April0ne
 *@date 2019/11/24 20:21
 */


import com.aprilhealth.pojo.Permission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionDao {
//    /**
//     * 查询permission_id集合: 从中间表 t_role_permission
//     * @param role_id
//     * @return
//     */
//    @Select("select permission_id from t_role_permission  where role_id = #{role_id}")
//    List<Integer> findPermissionIdByRole(Integer role_id);
//
//    /**
//     * 查询permission: 根据permissionId
//     * @param permissionId
//     * @return
//     */
//    @Select("select * from t_permission where id = #{id}")
//    Permission findPermissionById(Integer permissionId);


    //整合上述两个sql
    @Select("select tp.* from t_role_permission trp ,t_permission tp  where trp.role_id = #{role_id} and tp.id = trp.permission_id")
    Set<Permission> findPermissionIdByRoleId(Integer role_id);

    /**
     * 查询所有权限
     * @return
     */
    @Select("select * from t_permission")
    List<Permission> findAllPermission();
}
