package com.liu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.liu.constant.MessageConstant;
import com.liu.dao.MemberDao;
import com.liu.dao.OrderDao;
import com.liu.dao.OrderSettingDao;
import com.liu.entity.Result;
import com.liu.pojo.Member;
import com.liu.pojo.Order;
import com.liu.pojo.OrderSetting;
import com.liu.service.OrderService;
import com.liu.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    //体检预约
    @Override
    public Result order(Map map) throws Exception {
        //检查预约日期是否有预约设置
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting == null) {
            //没有预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //当前日期预约日期是否已满
        int number = orderSetting.getNumber();//可用人数
        int reservations = orderSetting.getReservations();//已经预约人数
        if (reservations >= number) {
            //预约人数已满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //判断用户是否是重复预约
        String telephone = (String) map.get("telephone");//获取页面输入手机号
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            //查找到用户表有此用户
            //判断是否重复预约
            Integer id = member.getId();
            Date order_Date = DateUtils.parseString2Date(orderDate);
            Integer setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(id, order_Date, setmealId);
            //根据条件查询
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0) {
                //重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);//已经完成预约
            }
        } else {
            //判断是否是会员,如果是则直接完成预约,如果不是,则进行注册
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setName((String) map.get("name"));
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);//完成会员注册
        }
        //预约成功,更新已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);//未到诊状态
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        //更新人数
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    /**
     * map里面信息:
     * 1,体检人姓名
     * 2,预约日期
     * 3,套餐名称
     * 4,预约类型
     * @param orderId
     * @return
     * @throws Exception
     */
    @Override
    public Map findById(Integer orderId) throws Exception{
        Map map = orderDao.findById4Detail(orderId);
        if (map!=null){
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return null;
    }
}
