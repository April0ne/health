package com.aprilhealth.sql;
/*
 *@author April0ne
 *@date 2019/11/14 16:44
 */


import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import org.apache.ibatis.jdbc.SQL;

public class SetmealSqlProvider {
    /**
     * private Integer id;
     private String name;
     private String code;
     private String helpCode;
     private String sex;//套餐适用性别：0不限 1男 2女
     private String age;//套餐适用年龄
     private Float price;//套餐价格
     private String remark;
     private String attention;
     private String img;//套餐对应图片存储路径
     private List<CheckGroup> checkGroups;//体检套餐对应的检查组，多对多关系
     */

    /**
     * 分页查询(使用分页插件)
     * @return
     */
    public String findPageFromSetmeal(String queryString){
        String sql = new SQL(){
            {
                SELECT("id","name","code","helpCode","sex","age","price","remark","attention","img");
                FROM("t_setmeal");
                if (queryString != null && queryString.length() > 0) {
                    WHERE("code=#{queryString} or name=#{queryString} or helpCode=#{queryString}");
                }
            }
        }.toString();
        return sql;
    }



}
