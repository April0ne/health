package com.aprilhealth.dao;
/*
 *@author April0ne
 *@date 2019/11/18 11:17
 *处理体检套餐数据的dao接口
 */

import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.Setmeal;
import com.aprilhealth.sql.SetmealSqlProvider;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SetmealDao {

    /**
     * 分页查询(使用分页插件)
     * @param queryString
     * @return
     */
    @SelectProvider(type = SetmealSqlProvider.class,method = "findPageFromSetmeal")
    Page<Setmeal> findPageFromSetmeal(String queryString);

    /**
     * 新增套餐
     * @param setmeal
     * @return
     */
    @Insert("insert into t_setmeal (code,name,helpCode,sex,age,price,remark,attention,img) values (#{code},#{name},#{helpCode},#{sex},#{age},#{price},#{remark},#{attention},#{img})")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=int.class)
    Integer addSetmeal(Setmeal setmeal);

    /**
     * 新增关联表数据(t_setmeal_checkgroup)
     * @param map
     * @return
     */
    @Insert("insert into t_setmeal_checkgroup (setmeal_id,checkgroup_id) values (#{setmeal_id},#{checkgroup_id})")
    Integer addToT_setmeal_checkgroup(Map map);


    /**
     * mobile : 查询所有套餐
     * @return
     */
    @Select("select  * from t_setmeal")
    List<Setmeal> findAllSetmeal();

    /**
     * mobile : 查询套餐之ByID:查看套餐详情
     * @param id
     * @return
     */
    @Select("select * from t_setmeal where id = #{id}")
    Setmeal findById(Integer id);

    /**
     * 套餐预约占比 : 不区分时间,统计所有订单关联的套餐,并统计被预约套餐的数量
     * @return
     */
    @Select("select ts.`name`,count(tr.setmeal_id) as value from t_order tr,t_setmeal ts WHERE tr.setmeal_id = ts.id GROUP BY tr.setmeal_id order by tr.setmeal_id desc")
    List<Map<String,Object>> findSetmealReservation();
}
