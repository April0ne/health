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
import com.aprilhealth.pojo.Role;
import com.aprilhealth.pojo.User;
import com.aprilhealth.security.SpringSecurityUserService;
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

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;

    /**
     * 获取用户信息
     * @return
     * @throws Exception
     */
    @GetMapping("/getUsername")
    public Result getUsername() throws Exception{
        Result result = null;
        try{
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            result = new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }catch (Exception e) {
            result = new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }

        return result;
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     * @throws Exception
     */
    @PostMapping("/findPageUser")
    @PreAuthorize("hasAnyAuthority('USER_QUERY')")
    public Result findPageUser(@RequestBody QueryPageBean queryPageBean) throws Exception{
        Result result = null;
        try{
            PageResult pageResult = userService.findPageUser(queryPageBean);
            result = new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,pageResult);
        }catch (Exception e) {
            result = new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
        return result;
    }

    /**
     * 查询所有角色
     * @return
     * @throws Exception
     */
    @GetMapping("/findAllRoles")
    public Result findAllRoles() throws Exception{
        Result result = null;
        try {
            List<Role> roleList = userService.findAllRoles();
            result = new Result(true, MessageConstant.QUERY_USER_SUCCESS,roleList);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
        return result;
    }

    /**
     * 新增员工
     * @param user
     * @param rolesId
     * @return
     * @throws Exception
     */
    @PostMapping("/addUser")
    @PreAuthorize("hasAnyAuthority('USER_ADD')")
    public Result addUser(@RequestBody User user,Integer[] rolesId) throws Exception{
        Result result = null;
        boolean flag = false;
        try {
            flag = userService.addUser(user,rolesId);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, MessageConstant.ADD_USER_FAIL);
        }
        if (flag) {
            result = new Result(true, MessageConstant.ADD_USER_SUCCESS);
        }else {
            result = new Result(false, MessageConstant.ADD_USER_FAIL);
        }

        return result;
    }

}
