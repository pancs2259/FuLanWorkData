package com.company.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class FuLanDataService {

    //获取复深蓝加班信息列表总页数
    public static int getOverTimePageTotal(String cookie) {
        String url = "https://erp.fulan.com.cn/admin/overtime/search";
        try {
            String request = "page=1";
            String result = doPost2(url, "UTF-8", cookie, request,"application/x-www-form-urlencoded;charset=UTF-8");
            JSONObject response = JSONObject.parseObject(result);
            if(response != null && "200".equals(response.getString("code"))){
                JSONObject data = response.getJSONObject("data");
                int pageTotal = data.getIntValue("pageTotal");
                return pageTotal;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取总加班时长
    public static BigDecimal getSumOverTime(String cookie){
        JSONArray jsonArray = getFuLanOverTimeAllData(cookie);
        BigDecimal sumHours = BigDecimal.ZERO;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject result = jsonArray.getJSONObject(i);
            System.out.println("加班列表:"+result.getString("overtimeDate")+",加班时长(小时):"+result.getString("hours"));
            BigDecimal bigDecimal = new BigDecimal(result.getString("hours"));
            sumHours = sumHours.add(bigDecimal);
        }
        return sumHours;
    }

    //获取所有复深蓝加班信息列表
    public static JSONArray getFuLanOverTimeAllData(String cookie) {
        JSONArray objects = new JSONArray();
        try {
            int pageTotal = getOverTimePageTotal(cookie);
            for (int i = 1; i < pageTotal + 1; i++) {
                String request = "page=" + i;
                JSONArray jsonArray = getFuLanOverTimeData(request, cookie);
                if(jsonArray != null){
                    objects.addAll(jsonArray);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    //获取单页复深蓝加班信息列表
    public static JSONArray getFuLanOverTimeData(String request,String cookie) {
        String url = "https://erp.fulan.com.cn/admin/overtime/search";
        try {
            String result = doPost2(url, "UTF-8", cookie, request,"application/x-www-form-urlencoded;charset=UTF-8");
            JSONObject response = JSONObject.parseObject(result);
            if(response != null && "200".equals(response.getString("code"))){
                JSONObject data = response.getJSONObject("data");
                JSONArray overtimeVoList = data.getJSONArray("overtimeVoList");
                if(overtimeVoList != null && overtimeVoList.size() > 0){
                    return overtimeVoList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doPost(String url, String charset, String cookie, String request,String contentType) throws Exception {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
//        postMethod.addRequestHeader("accept", "*/*");
        postMethod.addRequestHeader("connection", "Keep-Alive");
        //设置cookie
        postMethod.addRequestHeader("Cookie",cookie);
        //设置json格式传送
        postMethod.addRequestHeader("Content-Type", contentType);
        //必须设置下面这个Header
        postMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
        //添加请求参数
        StringRequestEntity stringRequestEntity = new StringRequestEntity(request, contentType,charset);
        postMethod.setRequestEntity(stringRequestEntity);

        String res = "";
        try {
            int code = httpClient.executeMethod(postMethod);
            if (code == 200){
                res = postMethod.getResponseBodyAsString();
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String doPost2(String reqUrl, String charset, String cookie, String request,String contentType) throws Exception {
        URL url = new URL(reqUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Cookie",cookie);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        //请求
        connection.connect();

        OutputStream out = connection.getOutputStream();
        out.write(request.getBytes(charset));
        out.flush();
        out.close();

        int responseCode = connection.getResponseCode();
        if(responseCode == 200){
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while (-1 != (len = inputStream.read(bytes))){
                byteArrayOutputStream.write(bytes,0,len);
            }
            inputStream.close();
            return new String(byteArrayOutputStream.toByteArray(),"UTF-8");
        }else{
            throw new RuntimeException("请求url异常");
        }
    }

    //获取复深蓝加班信息列表总页数
    public static int getVacationPageTotal(String cookie) {
        String url = "https://erp.fulan.com.cn/admin/vacation/search";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("page","1");
            String result = doPost(url, "UTF-8", cookie, jsonObject.toJSONString(),"application/json;charset=UTF-8");
            JSONObject response = JSONObject.parseObject(result);
            if(response != null && "200".equals(response.getString("code"))){
                JSONObject data = response.getJSONObject("data");
                int pageTotal = data.getIntValue("pageTotal");
                return pageTotal;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取单页复深蓝假期信息列表
    public static JSONArray getFuLanVacationData(String request,String cookie) {
        String url = "https://erp.fulan.com.cn/admin/vacation/search";
        try {
            String result = doPost2(url, "UTF-8", cookie, request,"application/x-www-form-urlencoded;charset=UTF-8");
            JSONObject response = JSONObject.parseObject(result);
            if(response != null && "200".equals(response.getString("code"))){
                JSONObject data = response.getJSONObject("data");
                JSONArray vacationList = data.getJSONArray("vacationList");
                if(vacationList != null && vacationList.size() > 0){
                    return vacationList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取所有复深蓝假期信息总列表
    public static JSONArray getFuLanVacationAllData(String cookie) {
        JSONArray objects = new JSONArray();
        try {
            int pageTotal = getVacationPageTotal(cookie);
            for (int i = 1; i < pageTotal + 1; i++) {
                String request = "page=" + i;
                JSONArray jsonArray = getFuLanVacationData(request, cookie);
                if(jsonArray != null){
                    objects.addAll(jsonArray);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    //获取总调休时长
    public static BigDecimal getSumVacation(String cookie){
        JSONArray jsonArray = getFuLanVacationAllData(cookie);
        BigDecimal sumHours = BigDecimal.ZERO;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject result = jsonArray.getJSONObject(i);
            System.out.println("休假起始日期:"+result.getString("startDate")+",假期类型:"+result.getString("vacationTypeName")+",休假时长(小时):"+result.getString("days"));
            BigDecimal bigDecimal = new BigDecimal(result.getString("days"));
            String vacationType = result.getString("vacationTypeId");//休假类型 V00000009:调休 V00000001:年休假
            if(!"V00000009".equals(vacationType)){
                continue;
            }
            sumHours = sumHours.add(bigDecimal);
        }
        return sumHours;
    }
}
