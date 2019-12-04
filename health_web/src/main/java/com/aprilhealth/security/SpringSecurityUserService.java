package com.aprilhealth.security;
/*
 *@author April0ne
 *@date 2019/11/24 19:52
 */


import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.pojo.Permission;
import com.aprilhealth.pojo.Role;
import com.aprilhealth.pojo.User;
import com.aprilhealth.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService  implements UserDetailsService{

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return  null;
        }

        List<GrantedAuthority> permissionList = new ArrayList<>();
        //查询用户所属角色
        Set<Role> userRoles = user.getRoles();
        for (Role role : userRoles) {
            //查询角色有什么权限
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //将权限添加至permissionList
                permissionList.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,user.getPassword(),permissionList);
        return userDetails;
    }
}
