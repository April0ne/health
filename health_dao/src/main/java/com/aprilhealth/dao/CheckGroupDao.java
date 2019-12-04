package com.aprilhealth.dao;
/*
 *@author April0ne
 *@date 2019/11/14 15:22
 *处理检查组操作dao的需求
 */


import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.CheckItem;
import com.aprilhealth.sql.CheckGroupSqlProvider;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CheckGroupDao {

    //注解动态sql test
    @SelectProvider(type= CheckGroupSqlProvider.class,method = "test")
    List<CheckGroup> test();

    /**
     * 查询检查组根据id
     * @param checkGroupId CheckGroup表id
     * @return
     */
    @SelectProvider(type= CheckGroupSqlProvider.class,method = "findOneCheckGroup")
    CheckGroup findOneCheckGroup(Integer checkGroupId);

    /**
     * 查询所有检查组数据
     * @return
     */
    @Select("select id,code,name,remark from t_checkgroup")
    List<CheckGroup> findAllCheckGroup();


    /**
     * 分页查询(使用分页插件)
     * @param queryString 关键词
     * @return
     */
    @SelectProvider(type= CheckGroupSqlProvider.class,method = "findPageCheckGroup")
    Page<CheckGroup> findPageCheckGroup(String queryString);


    /**
     * 根据条件查询总项目检查组数
     * @param queryPageBean
     * @return
     */
    @SelectProvider(type= CheckGroupSqlProvider.class,method = "findSumCheckGroup")
    List<CheckGroup> findSumCheckGroup(QueryPageBean queryPageBean);


    /**
     * 查CheckGroupID信息:从CheckGroup和Setmeal的中间表
     * @param setmeal_id
     * @return
     */
    @Select("select checkgroup_id from t_setmeal_checkgroup where setmeal_id = #{setmeal_id}")
    List<Integer> findCheckGroupIDBySetmealId(Integer setmeal_id);


    /**
     * 更新数据
     * @param checkGroup
     * @return
     */
    @UpdateProvider(type= CheckGroupSqlProvider.class,method = "updateCheckGroup")
    Integer updateCheckGroup(CheckGroup checkGroup);

    /**
     * 新增数据
     * @param checkGroup
     * @return
     */
    @Insert("insert into t_checkgroup (code,name,helpCode,sex,remark,attention) values(#{code},#{name}, #{helpCode}, #{sex}, #{remark}, #{attention})")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=int.class)
    Integer addCheckGroup(CheckGroup checkGroup);

    /**
     * 新增数据(往关联表-t_checkgroup_checkitem,中插入数据)
     * @param map
     * @return
     */
    @Insert("insert into t_checkgroup_checkitem (checkgroup_id,checkitem_id) values(#{checkgroup_id},#{checkitem_id})")
    Integer addToT_checkgroup_checkitem(Map map);


    /**
     * 删除之t_checkgroup表记录
     * @param id
     * @return
     */
    @Delete("delete from t_checkgroup where id=#{id}")
    Integer deleteFromCheckGroup(Integer id);

    /**
     * 删除t_checkgroup_checkitem中checkgroup相关记录
     * @param id
     * @return
     */
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id = #{id} ")
    Integer deleteFromT_checkgroup_checkitem(Integer id);

    /**
     * 查询checkgroup与setmeal的关系表
     * @param id
     * @return
     */
    @Select("select count(*) from t_setmeal_checkgroup where checkgroup_id = #{id}")
    long findCountByT_setmeal_checkgroup(Integer id);



}
