package com.liu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.liu.constant.MessageConstant;
import com.liu.entity.Result;
import com.liu.service.MemberService;
import com.liu.service.SetMealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 图形报表控制器
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;

    //会员数量折线图
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<String> monthList = new ArrayList<>();
        //计算过去12个月
        Calendar calendar = Calendar.getInstance();//获得日历,默认时间为当前
        calendar.add(Calendar.MONTH, -12);//按照月份向前推12个月,然后一个月一个月向后推
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            Date date = calendar.getTime();
            monthList.add(new SimpleDateFormat(("yyyy-MM")).format(date));//过去的12个月份
        }
        map.put("months", monthList);
        List<Integer> memberCount = memberService.findMemberCountByMonth(monthList);
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    /**
     * 预约占比饼形图
     *
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        Map<String, Object> data = new HashMap<>();
        try {
            List<Map<String, Object>> setmealCount = setMealService.findSetmealCount();
            data.put("setmealCount", setmealCount);
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> map : setmealCount) {
                String name = (String) map.get("name");//套餐名称
                setmealNames.add(name);
            }
            data.put("setmealNames", setmealNames);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }
}
