package com.company;

import com.company.service.FuLanDataService;

import java.math.BigDecimal;

public class Test {

    @org.junit.Test
    public void test(){
        String cookie = "sid=dded7f43-4c67-4090-8855-1fafcbc9fd2b; JSESSIONID=1BE6C0C05EE3015A3496925AC4722106";
        FuLanDataService service = new FuLanDataService();
        BigDecimal sumOverTime = service.getSumOverTime(cookie);
        System.out.println("总加班时间(小时)："+sumOverTime);
        BigDecimal sumVacation = service.getSumVacation(cookie);
        System.out.println("总调休时间(小时)："+sumVacation+"(不包含年休假)");
        System.out.println("剩余调休时间(小时)："+sumOverTime.subtract(sumVacation)
                +",共"+(sumOverTime.subtract(sumVacation)).divide(new BigDecimal(8))+"天");
    }
}
