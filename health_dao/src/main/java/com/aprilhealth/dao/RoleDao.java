package com.aprilhealth.dao;
/*
 *@author April0ne
 *@date 2019/11/24 20:20
 */


import com.aprilhealth.pojo.Permission;
import com.aprilhealth.pojo.Role;
import com.aprilhealth.sql.RoleSqlProvider;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface RoleDao {

//    /**
//     * 查询role_id集合: 从中间表 t_user_role
//     * @param user_id
//     * @return
//     */
//    @Select("select role_id from t_user_role where user_id = #{user_id}")
//    List<Integer> findRoleIdByUser(Integer user_id);
//
//    /**
//     * 查询role
//     * @param roleId
//     * @return
//     */
//    @Select("select * from t_role where id = #{id}")
//    Role findRoleById(Integer roleId);


    /**
     * 登录验证权限,封装roles
     * @param user_id
     * @return
     */
    @Select("select tr.* from t_role tr ,t_user_role tur where  tur.user_id = #{userId} and tur.role_id = tr.id")
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "roleStatus",column = "roleStatus"),
            @Result(property = "createTime",column = "createTime"),
            @Result(property = "updateTime",column = "updateTime"),
            @Result(property = "menus",column = "id",many = @Many(
                    select="com.aprilhealth.dao.MenuDao.findMenuIdByRoleId",fetchType = FetchType.LAZY
            )),
            @Result(property = "permissions",column = "id",many = @Many(
                    select="com.aprilhealth.dao.PermissionDao.findPermissionIdByRoleId",fetchType = FetchType.LAZY
            ))
    })
    Set<Role> findRoleByUserIdForLogin(Integer user_id);

    /**
     * 查询用户所拥有的角色
     * @param user_id
     * @return
     */
    @Select("select tr.* from t_role tr ,t_user_role tur where  tur.user_id = #{userId} and tur.role_id = tr.id and tr.roleStatus = 1")
    Set<Role> findRoleByUserId(Integer user_id);


    /**
     * 分页查询
     * @param queryString
     * @return
     */
    @SelectProvider(value = RoleSqlProvider.class,method = "findPageRole")
    Page<Role> findPageRole(String queryString);

    /**
     * 新增角色
     * @param role
     * @return
     */
    @Insert("insert into t_role (name,keyword,description,roleStatus,createTime,updateTime) values (#{name},#{keyword},#{description},#{roleStatus},#{createTime},#{updateTime})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()",before = false,keyProperty = "id",resultType = Integer.class)
    Integer addRole(Role role);


    /**
     * 往中间表t_role_permission添加数据
     * @param map
     */
    @Insert("insert into t_role_permission (role_id,permission_id) values (#{role_id},#{permission_id})")
    void addRole_idAndPermission_id(Map map);

    /**
     * 查询所有角色(状态为可用的角色)
     * @return
     */
    @Select("select * from t_role where roleStatus = 1")
    List<Role> findAllRoles();

    /**
     * 查询角色(更新之回显数据)
     * @param roleId
     * @return
     */
    @Select("select * from t_role where id = #{roleId}")
    Role findRoleById(Integer roleId);

    /**
     * 查询角色已有权限
     * @param roleId
     * @return
     */
    @Select("select tp.id from t_role_permission trp,t_permission tp where trp.role_id = #{roleId} and trp.permission_id = tp.id")
    List<Integer> findRoleHadPermission(Integer roleId);

    /**
     * 从角色权限中间表删除数据:根据roleId
     * @param role_id
     */
    @Delete("delete from t_role_permission where role_id = #{role_id}")
    void deleteRoleAndPermission(Integer role_id);

    /**
     * 更新角色表信息
     * @param role
     */
    @Update("update t_role set name =#{name} ,roleStatus =#{roleStatus} ,updateTime = #{updateTime} where id = #{id}")
    void updateRole(Role role);

    /**
     * 往角色权限中间表添加数据:根据roleId
     * @param id
     * @param permissionId
     */
    @Insert("insert into t_role_permission (role_id,permission_id) values(#{role_id},#{permission_id})")
    void addRoleAndPermission(@Param(value = "role_id") Integer id, @Param(value = "permission_id")Integer permissionId);

    /**
     * 查询角色是否有用户(在职)使用.
     * @param roleId
     * @return
     */
    @Select("select tu.id,tu.username,tu.station from t_user_role tur, t_user tu where tur.role_id = #{roleId} and tu.id = tur.user_id and tu.station = #{userStation}")
    Long roleHadUserUse(@Param(value = "roleId")Integer roleId,@Param(value = "userStation")Integer userStation);

    /**
     * 删除中间表t_user_role数据
     * @param roleId
     */
    @Delete("delete from t_user_role where role_id = #{roleId}")
    void deleteRoleFromUser_Role(Integer roleId);

    /**
     * 删除中间表t_role_permission数据
     * @param roleId
     */
    @Delete("delete from t_role_permission where role_id = #{roleId}")
    void deleteRoleFromRole_Permission(Integer roleId);

    /**
     * 删除中间表t_role_menu数据
     * @param roleId
     */
    @Delete("delete from t_role_menu where role_id = #{roleId}")
    void deleteRoleFromRole_Menu(Integer roleId);

    /**
     * 删除表t_role数据
     * @param roleId
     */
    @Delete("delete from t_role where id = #{roleId}")
    void deleteRole(Integer roleId);
}
