package com.company;

import com.company.service.FuLanDataService;

import java.math.BigDecimal;

public class Test {

    @org.junit.Test
    public void test(){
        String cookie = "sid=6818eff4-b324-43e5-b8d3-07e38794582e; JSESSIONID=387A148A0FED17D29DFE8CC445E048B2";
        FuLanDataService service = new FuLanDataService();
        BigDecimal sumOverTime = service.getSumOverTime(cookie);
        System.out.println("总加班时间(小时)："+sumOverTime);
        BigDecimal sumVacation = service.getSumVacation(cookie);
        System.out.println("总调休时间(小时)："+sumVacation+"(不包含年休假)");
        System.out.println("剩余调休时间(小时)："+sumOverTime.subtract(sumVacation)
                +",共"+(sumOverTime.subtract(sumVacation)).divide(new BigDecimal(8))+"天");
    }
}
