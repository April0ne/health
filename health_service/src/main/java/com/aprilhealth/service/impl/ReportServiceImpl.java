package com.aprilhealth.service.impl;
/*
 *@author April0ne
 *@date 2019/11/26 13:50
 */


import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.dao.MemberDao;
import com.aprilhealth.dao.OrderDao;
import com.aprilhealth.dao.SetmealDao;
import com.aprilhealth.pojo.Order;
import com.aprilhealth.service.ReportService;
import com.aprilhealth.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Transactional
@Service(protocol = "dubbo",interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 查询运营数据
     * @return
     */
    @Override
    public Map<String, Object> getBusinessData() throws Exception {
        Map<String, Object> map = new HashMap<>();
        //封装日期
        //今日
        Date today = DateUtils.getToday();
        map.put("reportDate",DateUtils.parseDate2String(today));

        //封装会员数据
        Long memberCountToday = memberDao.findTodayMember(DateUtils.parseDate2String(today));
        Long memberCount = memberDao.findCountMember();
        Long memberCountOfWeek = memberDao.findCountMemberBetweenDate(DateUtils.parseDate2String(DateUtils.getFirstDayOfWeek(today)),DateUtils.parseDate2String(DateUtils.getLastDayOfWeek(today)));
        Long memberCountOfMonth = memberDao.findCountMemberBetweenDate(DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth()),DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth()));
        map.put("todayNewMember",memberCountToday);
        map.put("totalMember",memberCount);
        map.put("thisWeekNewMember",memberCountOfWeek);
        map.put("thisMonthNewMember",memberCountOfMonth);

        //封装订单数据
        Long orderCountYesterday = orderDao.findYesterdayOrder(DateUtils.parseDate2String(DateUtils.getYesterday()));
        Long orderCountYesterdayOrderStatus = orderDao.findYesterdayOrderStatus(DateUtils.parseDate2String(DateUtils.getYesterday()),Order.ORDERSTATUS_YES);
        Long orderCountOfWeek = orderDao.findCountOrderBetweenDate(DateUtils.parseDate2String(DateUtils.getFirstDayOfWeek(today)),DateUtils.parseDate2String(DateUtils.getLastDayOfWeek(today)));
        Long orderCountWeekOrderStatus = orderDao.findOrderBetweenDateStatus(DateUtils.parseDate2String(DateUtils.getFirstDayOfWeek(today)),DateUtils.parseDate2String(DateUtils.getLastDayOfWeek(today)),Order.ORDERSTATUS_YES);
        Long orderCountOfMonth = orderDao.findCountOrderBetweenDate(DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth()),DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth()));
        Long orderCountMonthOrderStatus = orderDao.findOrderBetweenDateStatus(DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth()),DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth()),Order.ORDERSTATUS_YES);
        map.put("todayOrderNumber",orderCountYesterday);
        map.put("todayVisitsNumber",orderCountYesterdayOrderStatus);
        map.put("thisWeekOrderNumber",orderCountOfWeek);
        map.put("thisWeekVisitsNumber",orderCountWeekOrderStatus);
        map.put("thisMonthOrderNumber",orderCountOfMonth);
        map.put("thisMonthVisitsNumber",orderCountMonthOrderStatus);

        //封装套餐数据:通过sql直接查询结果
        List<Map<String,Object>> setmealList = orderDao.findHotSetmeal();

        /*
        可以直接通过sql封装,以下是通过代码封装
        List<Map<String,Object>> setmealList = setmealDao.findSetmealReservation();
        for (int i = 0; i < setmealList.size() ; i++) {
            Long orderAll = orderDao.findAll();
            Map<String,Object> hotSetmealMap = setmealList.get(i);
            Long SetmealOrderValue = (Long)hotSetmealMap.get("value");
            double proportion = SetmealOrderValue/orderAll;
            hotSetmealMap.put("proportion",proportion);
        }*/
        map.put("hotSetmeal",setmealList);

        return map;
    }

}
