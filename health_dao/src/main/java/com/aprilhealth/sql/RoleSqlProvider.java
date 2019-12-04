package com.aprilhealth.sql;
/*
 *@author April0ne
 *@date 2019/11/14 16:44
 */


import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import org.apache.ibatis.jdbc.SQL;

public class RoleSqlProvider {

    /**
     * 分页查询(使用分页插件)
     * @return
     */
    public String findPageRole(String queryString){
        String sql = new SQL(){
            {
                SELECT("id","name","keyword","description","roleStatus","createTime","updateTime");
                FROM("t_role");
                if (!"%null%".equals(queryString) && queryString.length() > 0 &&  queryString != null) {
                    WHERE("name like #{queryString}");


                }
            }
        }.toString();
        return sql;
    }


}
