package com.liu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.liu.constant.MessageConstant;
import com.liu.entity.Result;
import com.liu.pojo.OrderSetting;
import com.liu.service.OrderSettingService;
import com.liu.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }
}
