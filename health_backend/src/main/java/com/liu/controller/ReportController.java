package com.liu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.liu.constant.MessageConstant;
import com.liu.entity.Result;
import com.liu.service.MemberService;
import com.liu.service.ReportService;
import com.liu.service.SetMealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
    @Reference
    private ReportService reportService;

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

    /**
     * reportDate: null,
     * todayNewMember: 0,
     * totalMember: 0,
     * thisWeekNewMember: 0,
     * thisMonthNewMember: 0,
     * todayOrderNumber: 0,
     * todayVisitsNumber: 0,
     * thisWeekOrderNumber: 0,
     * thisWeekVisitsNumber: 0,
     * thisMonthOrderNumber: 0,
     * thisMonthVisitsNumber: 0,
     * hotSetmeal: [
     * {name: '阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐', setmeal_count: 200, proportion: 0.222},
     * {name: '阳光爸妈升级肿瘤12项筛查体检套餐', setmeal_count: 200, proportion: 0.222}
     * ]
     *
     * @return
     */
    @RequestMapping("/getBusinessReportData")

    public Result getBusinessReportData() {
        Map<String, Object> reportData = null;
        try {
            reportData = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, reportData);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出数据Excel
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //取出数据
            Map<String, Object> data = reportService.getBusinessReportData();
            String reportDate = (String) data.get("reportDate");
            Integer todayNewMember = (Integer) data.get("todayNewMember");
            Integer totalMember = (Integer) data.get("totalMember");
            Integer thisWeekNewMember = (Integer) data.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) data.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) data.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) data.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) data.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) data.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) data.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) data.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) data.get("hotSetmeal");

            String templatePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";// File.separator 自动适应不同系统的路径斜线
            //基于提供的模板,创建文件
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(templatePath)));
            XSSFSheet sheet = excel.getSheetAt(0);
            //加载各种单元格数据
            XSSFRow row = sheet.getRow(2);//第三行
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for (Map map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //使用输出流下载
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            excel.write(out);
            out.flush();
            out.close();
            excel.close();
            return null;//不返回数据,只进行下载

        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    //导出运营数据到PDF文件并提供客户端下载
    @RequestMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String,Object> result = reportService.getBusinessReportData();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //动态获取pdf模板文件绝对磁盘路径
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jasper";

            //编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);

            //填充数据---使用JavaBean数据源方式填充
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperPath,result,
                            new JRBeanCollectionDataSource(hotSetmeal));

            //创建输出流，用于从服务器写数据到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");

            //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint,out);

            out.flush();
            out.close();

            return null;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
