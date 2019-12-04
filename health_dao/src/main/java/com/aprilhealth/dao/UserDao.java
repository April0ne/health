package com.aprilhealth.dao;
/*
 *@author April0ne
 *@date 2019/11/24 20:08
 */

import com.aprilhealth.pojo.User;
import com.aprilhealth.sql.UserSqlProvider;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao {

    /**
     * 查询用户: 根据用户名
     * @param username 用户名
     * @return
     */
    @Select("select * from t_user where username = #{value}")
    User findUserByUsername(String username);

//    /**
//     * 查询user_id集合: 从中间表 t_user_role
//     * @param roleId
//     * @return
//     */
//    @Select("select user_id from t_user_role where role_id = #{role_id}")
//    List<Integer> findUserIdByRole(Integer roleId);
//
//    /**
//     * 查询用户: 根据用户id
//     * @param userId
//     * @return
//     */
//    @Select("select * from t_user where id = #{id}")
//    User findUserById(Integer userId);




    /**
     * 分页查询
     * @param queryString
     * @return
     */
    @SelectProvider(value = UserSqlProvider.class,method = "findPageRole")
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "birthday",column = "birthday"),
            @Result(property = "gender",column = "gender"),
            @Result(property = "username",column = "username"),
            @Result(property = "station",column = "station"),
            @Result(property = "telephone",column = "telephone"),
            @Result(property = "roles",column = "id",many = @Many(
                    select = "com.aprilhealth.dao.RoleDao.findRoleByUserId"
            ))
    })
    Page<User> findPageUser(String queryString);

    /**
     * 新增员工
     * @param user
     * @return
     */
    @Insert("insert into t_user (birthday,gender,username,password,remark,station,telephone) values (#{birthday},#{gender},#{username},#{password},#{remark},#{station},#{telephone})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()",before = false,keyProperty = "id",resultType = Integer.class)
    Integer addUser(User user);

    /**
     * 往中间表添加数据
     * @param map
     */
    @Insert("insert into t_user_role (role_id,user_id) values (#{role_id},#{user_id})")
    void addUser_idAndRole_id(Map map);
}
