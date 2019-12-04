package com.aprilhealth.sql;
/*
 *@author April0ne
 *@date 2019/11/14 16:44
 */


import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import org.apache.ibatis.jdbc.SQL;
public class  CheckGroupSqlProvider {

    //注解动态sql test
    public String test(){
        String sql = new SQL(){
            {
                SELECT("id","code","name","helpCode","sex","remark","attention");
                FROM("t_checkgroup");
            }
        }.toString();
        return sql;
    }


    /**
     * 分页查询(使用分页插件)
     * @return
     */
    public String findPageCheckGroup(String queryString){
        String sql = new SQL(){
            {
                SELECT("id","code","name","helpCode","sex","remark","attention");
                FROM("t_checkgroup");
                if (queryString != null && queryString.length() > 0) {
                    WHERE("code=#{queryString} or name=#{queryString} or helpCode=#{queryString}");
                }
            }
        }.toString();
        return sql;
    }


    /**
     * 分页查询(不使用分页插件)
     * @return
     */
    public String findPageCheckGroupByList(QueryPageBean queryPageBean){
        String sql = new SQL(){
            {
                SELECT("id","code","name","helpCode","sex","remark","attention");
                FROM("t_checkgroup");
                if (queryPageBean.getQueryString() != null && queryPageBean.getQueryString().length() > 0) {
                    WHERE("code=#{queryString} or name=#{queryString} or helpCode=#{queryString}");
                }
                if (queryPageBean.getCurrentPage() >= 0 && queryPageBean.getCurrentPage() != null && queryPageBean.getPageSize() != null && queryPageBean.getPageSize() > 0) {
                    LIMIT(queryPageBean.getPageSize());//查这么多
                    OFFSET(queryPageBean.getCurrentPage());//从这开始查(不包含)
                }
            }
        }.toString();
        return sql;
    }

    /**
     * 根据条件查询总检查组数(分页查询)
     * @param queryPageBean
     * @return
     */
    public String findSumCheckGroup(QueryPageBean queryPageBean){
        String sql = new SQL(){{
            SELECT("id","code","name","helpCode","sex","remark","attention");
            FROM("t_checkgroup");
            if (queryPageBean.getQueryString() != null && queryPageBean.getQueryString().length() > 0) {
                WHERE("code=#{queryString} or name=#{queryString} or helpCode=#{queryString}");
            };
        }}.toString();
        return sql;
    }


    /**
     * 查询单个
     * @return
     */
    public String findOneCheckGroup(Integer id){
        String sql = new SQL(){{
            SELECT("id","code","name","helpCode","sex","remark","attention");
            FROM("t_checkgroup");
            WHERE("id = #{id}");

        }}.toString();
        return sql;
    }

    /**
     * 更新数据
     * @return
     */
    public String updateCheckGroup(CheckGroup checkGroup){
        String sql = new SQL().UPDATE("t_checkgroup").SET(
                "code=#{code}, name=#{name}, helpCode=#{helpCode}, sex=#{sex}, remark=#{remark}, attention=#{attention}").WHERE("id = #{id}").toString();
        return sql;
    }

}
