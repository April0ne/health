package com.aprilhealth.service.impl;
/*
 *@author April0ne
 *@date 2019/11/23 15:01
 */


import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.dao.MemberDao;
import com.aprilhealth.pojo.Member;
import com.aprilhealth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(protocol = "dubbo",interfaceClass = LoginService.class)
public class LoginServiceImpl implements LoginService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 登录校验
     * @param telephone
     * @return
     */
    @Override
    public boolean checkTelephone(String telephone) {
        boolean isLogin = false;
        //查看是否存在用户
        Member member = memberDao.findMemberByTel(telephone);
        if (member != null) {
            isLogin = true;
        }else {
            //不存在则创建用户
            //创建成功允许登录
            isLogin = memberDao.addMember(new Member(telephone)) > 0 ? true:false;
        }
        return isLogin;
    }
}
