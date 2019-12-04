package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/24 21:38
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.entity.Result;
import com.aprilhealth.pojo.Permission;
import com.aprilhealth.pojo.Role;
import com.aprilhealth.pojo.User;
import com.aprilhealth.service.RoleService;
import com.aprilhealth.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    @Reference
    private UserService userService;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     * @throws Exception
     */
    @PostMapping("/findPageRole")
    @PreAuthorize("hasAnyAuthority('ROLE_QUERY')")
    public Result findPageRole(@RequestBody QueryPageBean queryPageBean) throws Exception{
        Result result = null;
        try{
            PageResult pageResult = roleService.findPageRole(queryPageBean);
            result = new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,pageResult);
        }catch (Exception e) {
            result = new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
        return result;
    }

    /**
     * 查询所有权限
     * @return
     * @throws Exception
     */
    @GetMapping("/findAllPermission")
    @PreAuthorize("hasAnyAuthority('ROLE_QUERY')")
    public Result findAllPermission() throws Exception{
        Result result = null;
        try {
            List<Permission> permissionList = roleService.findAllPermission();
            result = new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }
        return result;
    }

    /**
     * 新增角色
     * @param permissionsId
     * @param role
     * @return
     * @throws Exception
     */
    @PostMapping("/addRole")
    @PreAuthorize("hasAnyAuthority('ROLE_ADD')")
    public Result addRole(@RequestBody Role role,Integer[] permissionsId) throws Exception{
        Result result = null;
        boolean flag = false;
        try {
            flag = roleService.addRole(role,permissionsId);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
        if (flag) {
            result = new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
        }else {
            result = new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }

        return result;
    }

    /**
     * 更新之回显数据
     * @param roleId
     * @return
     * @throws Exception
     */
    @PostMapping("/findRoleById/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_EDIT')")
    public Result findRoleById(@PathVariable(value = "id") Integer roleId ) throws Exception{
        Result result = null;
        if (roleId != null && roleId != 0) {
            Role role = null;
            try {
                role = roleService.findRoleById(roleId);
            } catch (Exception e) {
                e.printStackTrace();
                result = new Result(false, MessageConstant.QUERY_ROLE_FAIL);
            }
            if (role != null) {
                result = new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,role);
            }else {
                result = new Result(false, MessageConstant.QUERY_ROLE_FAIL);
            }
        }else {
            result = new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
        return result;
    }

    /**
     * 查询角色已有权限
     * @param roleId
     * @return
     * @throws Exception
     */
    @PostMapping("/findRoleHadPermission/{id}")
    //    @PreAuthorize("hasAnyAuthority('ROLE_EDIT')")
    public Result findRoleHadPermission(@PathVariable(value = "id") Integer roleId ) throws Exception{
        Result result = null;
        if (roleId != null && roleId != 0) {
            List<Integer> permissionIdList = null;
            try {
                permissionIdList = roleService.findRoleHadPermission(roleId);
            } catch (Exception e) {
                e.printStackTrace();
                result = new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
            }
            result = new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permissionIdList);
        }else {
            result = new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }
        return result;
    }

    /**
     * 更新角色
     * @param role
     * @param permissionsId
     * @return
     * @throws Exception
     */
    @PostMapping("/updateRole")
    public Result updateRole(@RequestBody Role role,Integer[] permissionsId) throws Exception{
        Result result = null;
        try {
            roleService.updateRole(role,permissionsId);
            reloadPermission();
            result = new Result(true,MessageConstant.EDIT_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false,MessageConstant.EDIT_ROLE_FAIL);
        }
        return result;
    }

    /**
     * 查询角色是否有用户(在职)使用.
     * @return
     * @throws Exception
     */
    @PostMapping("/roleHadUserUse/{id}")
    public Result roleHadUserUse(@PathVariable(value = "id") Integer roleId ) throws Exception{
        Result result = null;
        boolean isHad = false;
        try {
            isHad = roleService.roleHadUserUse(roleId);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }
        if (isHad) {
            result = new Result(true,MessageConstant.QUERY_ROLE_SUCCESS);
        }else {
            result = new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }
        return result;
    }

    /**
     * 删除角色
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteRole/{id}")
    public Result deleteRole(@PathVariable(value = "id") Integer roleId  ) throws Exception{
        Result result = null;
        try {
            roleService.deleteRole(roleId);
            result = new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false,MessageConstant.DELETE_ROLE_FAIL);
        }
        return result;
    }


    /**
     * 权限变更重新加载权限
     */
    public boolean reloadPermission(){
        try {
            // 得到当前的认证信息
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (String)auth.getPrincipal();
            System.out.println("username="+username);
            Object details = auth.getDetails();
            System.out.println("details=" + details);
            //  生成当前的所有授权
            List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());


            // 获取当前主体的所有权限
            User user = userService.findUserByUsername(username);
            if (user == null) {
                return  false;
            }
            //查询用户所属角色
            Set<Role> userRoles = user.getRoles();
            for (Role role : userRoles) {
                //查询角色有什么权限
                Set<Permission> permissions = role.getPermissions();
                for (Permission permission : permissions) {
                    //将权限添加至permissionList
                    updatedAuthorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
                }
            }

            // 生成新的认证信息
            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
            // 重置认证信息
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
