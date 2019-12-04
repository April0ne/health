package com.aprilhealth.service.impl;
/*
 *@author April0ne
 *@date 2019/11/27 12:59
 */


import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.dao.PermissionDao;
import com.aprilhealth.dao.RoleDao;
import com.aprilhealth.dao.UserDao;
import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.Permission;
import com.aprilhealth.pojo.Role;
import com.aprilhealth.pojo.User;
import com.aprilhealth.service.RoleService;
import com.aprilhealth.utils.DateUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service(protocol = "dubbo",interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserDao userDao;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPageRole(QueryPageBean queryPageBean) {
        Page<Role> rolePage = null;
        if (queryPageBean != null) {
            String queryString = "%" +  queryPageBean.getQueryString() + "%";
            PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
            rolePage = roleDao.findPageRole(queryString);
        }
        return new PageResult(rolePage.getTotal(),rolePage.getResult());
    }

    /**
     * 查询所有权限
     * @return
     */
    @Override
    public List<Permission> findAllPermission() {
        return permissionDao.findAllPermission();
    }

    /**
     * 新增角色
     * @param role
     * @param permissionsIds
     * @return
     */
    @Override
    public boolean addRole(Role role, Integer[] permissionsIds) {
        Date createAndUpdateTime = updateTime();
        role.setCreateTime(createAndUpdateTime);
        role.setUpdateTime(createAndUpdateTime);
        Integer isAdd = roleDao.addRole(role);
        if (isAdd != null && isAdd != 0) {
            addRole_idAndPermission_id(role,permissionsIds);
            return true;
        }
        return false;
    }

    /**
     * 获取格式化后的时间
     * @return
     */
    public Date updateTime(){
        Date createAndUpdateTime = null;
        //获取创建时间和修改时间(修改时间在创建时=创建时间)
        try {
            String dateStr = DateUtils.parseDate2String(DateUtils.getToday(), "yyyy-MM-dd HH:mm:ss");
            createAndUpdateTime = DateUtils.parseString2Date(dateStr,"yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createAndUpdateTime;
    }

    /**
     * 查询角色(更新之回显数据)
     * @param roleId
     * @return
     */
    @Override
    public Role findRoleById(Integer roleId) {
        return roleDao.findRoleById(roleId);
    }

    /**
     * 查询角色已有权限
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> findRoleHadPermission(Integer roleId) {
        return roleDao.findRoleHadPermission(roleId);
    }

    /**
     * 更新角色
     * @param role
     * @param permissionsId
     * @return
     */
    @Override
    public void updateRole(Role role, Integer[] permissionsId) throws RuntimeException {
        if (role != null) {
            //删除角色权限表信息
            deleteRoleAndPermission(role);
            Date updateTime = updateTime();
            role.setUpdateTime(updateTime);
            //更新角色表信息
            roleDao.updateRole(role);
            // 并往角色权限表新增数据
            addRoleAndPermission(role,permissionsId);
        }
    }

    /**
     * 查询角色是否有用户(在职)使用.
     * @param roleId
     * @return
     */
    @Override
    public boolean roleHadUserUse(Integer roleId) {
        Long isHad = roleDao.roleHadUserUse(roleId, User.USERSTATUS_YES);
        if (isHad != null && isHad > 0 ) {
            //有在职员工在使用,不可删除
            return false;
        }
        return true;
    }

    /**
     * 删除角色
     * @param roleId
     */
    @Override
    public void deleteRole(Integer roleId) throws RuntimeException{
        //删除中间表t_user_role数据
        roleDao.deleteRoleFromUser_Role(roleId);
        //删除中间表t_role_permission数据
        roleDao.deleteRoleFromRole_Permission(roleId);
//        int i = 10/0;
        //删除中间表t_role_menu数据
        roleDao.deleteRoleFromRole_Menu(roleId);
        //删除表t_role数据
        roleDao.deleteRole(roleId);
    }

    /**
     * 从角色权限中间表删除数据:根据roleId
     */
    public void deleteRoleAndPermission(Role role) throws RuntimeException{
        if (role.getId() != null && role.getId() != 0) {
            roleDao.deleteRoleAndPermission(role.getId());
        }
    }

    /**
     * 往角色权限中间表添加数据:根据roleId
     * @param role
     * @param permissionsId
     */
    public void addRoleAndPermission(Role role, Integer[] permissionsId) throws RuntimeException{
        if (permissionsId!= null && permissionsId.length > 0) {
            for (Integer permissionId : permissionsId) {
                roleDao.addRoleAndPermission(role.getId(),permissionId);
            }
        }
    }


    /**
     * 往中间表t_role_permission添加数据
     * @param role
     * @param permissionsIds
     * @return
     */
    public void addRole_idAndPermission_id(Role role, Integer[] permissionsIds){
        for (Integer permissionsId : permissionsIds) {
            Map map = new HashMap();
            map.put("role_id",role.getId());
            map.put("permission_id",permissionsId);
            roleDao.addRole_idAndPermission_id(map);
        }
    }
}
