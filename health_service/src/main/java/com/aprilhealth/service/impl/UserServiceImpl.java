package com.aprilhealth.service.impl;
/*
 *@author April0ne
 *@date 2019/11/24 19:55
 */


import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.dao.MenuDao;
import com.aprilhealth.dao.PermissionDao;
import com.aprilhealth.dao.RoleDao;
import com.aprilhealth.dao.UserDao;
import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.Menu;
import com.aprilhealth.pojo.Permission;
import com.aprilhealth.pojo.Role;
import com.aprilhealth.pojo.User;
import com.aprilhealth.service.UserService;
import com.aprilhealth.utils.DateUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service(protocol = "dubbo",interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private MenuDao menuDao;


    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPageUser(QueryPageBean queryPageBean) throws RuntimeException{
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        String queryString = "%" + queryPageBean.getQueryString() + "%";
        Page<User> userPage = userDao.findPageUser(queryString);
        return new PageResult(userPage.getTotal(),userPage.getResult());
    }

    /**
     * 查询角色
     * @return
     */
    @Override
    public List<Role> findAllRoles() {
        return roleDao.findAllRoles();
    }

    /**
     * 新增员工
     * @param user
     * @param rolesId
     * @return
     */
    @Override
    public boolean addUser(User user, Integer[] rolesId) {
        user.setPassword(encryption(user));
        Integer isAdd = userDao.addUser(user);
        if (isAdd != null && isAdd != 0) {
            addUser_idAndRole_id(user,rolesId);
            return true;
        }
        return false;
    }

    /**
     * 往中间表t_role_user添加数据
     * @param user
     * @param rolesId
     * @return
     */
    public void addUser_idAndRole_id(User user, Integer[] rolesId){
        for (Integer roleId : rolesId) {
            Map map = new HashMap();
            map.put("user_id",user.getId());
            map.put("role_id",roleId);
            userDao.addUser_idAndRole_id(map);
        }
    }

    /**
     * 密码加密
     * @param user
     * @return
     */
    public String encryption(User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return  encoder.encode(user.getPassword());
    }


    /**
     * 查询用户: 根据用户名
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username){
        User user = null;
        if (username != null && !"".equals(username)) {
            user = userDao.findUserByUsername(username);
            String station = user.getStation();
            if (user != null) {
                if (station != null && !"2".equals(station) && !"".equals(station)) {//判断是否在职
                    user.setRoles(findRoleIdByUser(user));
                }else {
                    user = null;
                }
            }
        }
        return user;
    }


    /**
     * 封装roles
     * @param user
     * @return
     */
    public Set<Role> findRoleIdByUser(User user){
        Set<Role> roleSet = new HashSet<>();
        if (user.getId() != 0 && user.getId() != null) {
                roleSet = roleDao.findRoleByUserIdForLogin(user.getId());
            }
        return roleSet;
    }



//    /**
//     * 封装menu
//     * @param role
//     * @return
//     */
//    public LinkedHashSet<Menu> findMenuByRole(Role role){
//        LinkedHashSet<Menu> menuLinkedHashSet =  new LinkedHashSet<>();
//        if (role.getId() != 0 && role.getId() != null) {
//            List<Integer> menuIdList = menuDao.findMenuIdByRole(role.getId());
//            if (menuIdList != null && menuIdList.size() > 0) {
//                for (Integer menuId : menuIdList) {
//                    Menu menu = menuDao.findMenuById(menuId);
//                    menuLinkedHashSet.add(menu);
//                }
//            }
//        }
//        return menuLinkedHashSet;
//    }

//    /**
//     * 封装permission
//     * @param role
//     * @return
//     */
//    public Set<Permission> findPermissionByRole(Role role) {
//        Set<Permission> permissionSet = new HashSet<>();;
//        if (role.getId() != 0 && role.getId() != null) {
//            List<Integer> permissionIdList = permissionDao.findPermissionIdByRole(role.getId());
//            if (permissionIdList != null && permissionIdList.size() > 0) {
//                for (Integer permissionId : permissionIdList) {
//                    Permission permission = permissionDao.findPermissionById(permissionId);
//                    permissionSet.add(permission);
//                }
//            }
//        }
//        return  permissionSet;
//    }
}
