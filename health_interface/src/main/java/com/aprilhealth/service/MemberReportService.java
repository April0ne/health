package com.aprilhealth.service;
/*
 *@author April0ne
 *@date 2019/11/25 15:02
 */


import java.util.List;

public interface MemberReportService {
    /**
     * 会员数量报表
     * @param list
     * @return
     */
    List<Long> findMemberCountByMonth(List<String> list);
}
