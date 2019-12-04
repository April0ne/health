package com.aprilhealth.sql;
/*
 *@author April0ne
 *@date 2019/11/14 16:44
 */


import org.apache.ibatis.jdbc.SQL;

public class UserSqlProvider {

    /**
     * 分页查询(使用分页插件)
     * @return
     */
    public String findPageRole(String queryString){
        String sql = new SQL(){
            {
                SELECT("id","birthday","gender","username","station","telephone");
                FROM("t_user");
                if (!"%null%".equals(queryString) && queryString.length() > 0 &&  queryString != null) {
                    WHERE("name like #{queryString}");
                }
            }
        }.toString();
        return sql;
    }

}
