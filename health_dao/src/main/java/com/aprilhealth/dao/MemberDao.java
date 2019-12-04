package com.aprilhealth.dao;
/*
 *@author April0ne
 *@date 2019/11/22 21:22
 */


import com.aprilhealth.pojo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MemberDao {
    /**
     * 查询:根据用户手机号
     * @param telephone
     * @return
     */
    @Select("select * from t_member where phoneNumber = #{value}")
    Member findMemberByTel(String telephone);

    /**
     * 查询:根据用户ID
     * @param memberId
     * @return
     */
    @Select("select * from t_member where id = #{memberId}")
    Member findMemberById(Integer memberId);

    /**
     * 新增会员:登录校验,如果用户不存在,自动新增用户
     * @param member
     * @return
     */
    @Insert("insert into t_member (phoneNumber) values (#{phoneNumber})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()" ,before = false, keyProperty = "id",resultType = Integer.class)
    Integer addMember(Member member);//如果要用SelectKey,就不能使用String telephone,而是应该使用插入对象类的实体类.不然执行@SelectKey,给id赋值时,会找不到id的set/get方法

    /**
     * 会员数量报表 : 如果不用like查询,用时间区间,则可以复用统计本周和本月数据的sql
     * @param monthStr
     * @return
     */
    @Select("select count(id) from t_member where regTime like #{value}")
    long findMemberCountByMonth(String monthStr);

    /**
     * 查询总会员数
     * @return
     */
    @Select("SELECT count(id) FROM t_member")
    Long findCountMember();

    /**
     * 查询某时间段的会员数
     * @param firstDay
     * @param lastDay
     * @return
     */
    @Select("SELECT count(id) FROM t_member  where regTime >= #{firstDay}  and   regTime <= #{lastDay}")
    Long findCountMemberBetweenDate(@Param(value = "firstDay") String firstDay,@Param(value = "lastDay") String lastDay);

    /**
     * 查询今天的会员数
     * @return
     */
    @Select("SELECT count(id) FROM t_member where regTime = #{value}")
    Long findTodayMember(String today);
}
