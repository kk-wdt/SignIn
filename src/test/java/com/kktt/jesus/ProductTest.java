package com.kktt.jesus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ProductTest extends BaseTest{
    public static final String PRODUCT_URL = DOMIN + "api/Product/QueryProductDetail";

    @Test
    public void getProduct(){
        Map<String,String> map = new HashMap<>();
        map.put("Version","1.0.0.0");
        map.put("RequestId", "5dc416a26128d");
        map.put("RequestTime","2020-11-05 14:28:50");
        map.put("Token",token);
        map.put("Sign",null);
        JSONObject message = new JSONObject();
        message.put("startTime","2021-01-01");
        message.put("EndTime","2021-01-28");
        message.put("PageIndex","1");
        message.put("Site","www.gotenchina.com");
        map.put("Message",message.toJSONString());
        map = sortMapByKey(map);
        String sign = encrypt(JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));
        String replaceBlankSign = replaceBlank(sign);
        map.put("Sign",replaceBlankSign);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        ResponseEntity<JSONObject> xx = RestTemplateUtils.post(PRODUCT_URL,headers, JSON.toJSONString(map, SerializerFeature.WriteMapNullValue), JSONObject.class);
        JSONArray productList = xx.getBody().getJSONObject("Message").getJSONArray("ProductInfoList");
        JSONObject productInfo = productList.getJSONObject(0);
        System.out.println(1);
    }

}
