package com.liu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.liu.constant.MessageConstant;
import com.liu.entity.Result;
import com.liu.pojo.OrderSetting;
import com.liu.service.OrderSettingService;
import com.liu.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置控制器
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;
    //预约设置Excel文件上传
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            List<String[]> excel = POIUtils.readExcel(excelFile);//使用POI解析表格数据
            List<OrderSetting> orderSettingList=new ArrayList<>();
            //遍历取出每一行数据,封装为OrderSetting
            for (String[] rowData : excel) {
                String date = rowData[0];
                String num = rowData[1];
                OrderSetting orderSetting = new OrderSetting(new Date(date), Integer.parseInt(num));
                orderSettingList.add(orderSetting);
            }
            orderSettingService.add(orderSettingList);
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }
    /**
     * 根据月份查询预约设置信息
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){//yyyy-MM
        try {
            List<Map<String,Integer>>dataList=orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,dataList);
        }catch (Exception e){
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }
    /**
     * 根据日期改变某一天的预约人数
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){//实体类中获取参数
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
