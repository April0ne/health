package com.aprilhealth.service.impl;
/*
 *@author April0ne
 *@date 2019/11/25 15:03
 */


import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.dao.MemberDao;
import com.aprilhealth.service.MemberReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(protocol = "dubbo",interfaceClass = MemberReportService.class)
public class MemberReportServiceImpl implements MemberReportService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 会员数量报表
     * @param list
     * @return
     */
    @Override
    public List<Long> findMemberCountByMonth(List<String> list) {
        List<Long>  memberCountList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (String monthStr : list) {
                monthStr+="%";
                long memberCount = memberDao.findMemberCountByMonth(monthStr);
                memberCountList.add(memberCount);
            }
        }
        return memberCountList;
    }
}
