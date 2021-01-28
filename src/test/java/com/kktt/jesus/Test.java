package com.kktt.jesus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class Test extends BaseTest{

    @org.junit.Test
    public void testReq(){
        String token = "yXmV1Xf1GFXGjTK8z2x4Bw==";
        String appKey = "z2o7Dn8w";

        //{"Version ":"1.0.0.0","RequestId":"5dc416a26128d","Message":"{}","RequestTime":"2020-11-05T14:28:50","Token":"yXmV1Xf1GFXGjTK8z2x4Bw\u003d\u003d","Sign":"haqQVVoX9Le5PAV5G2RWwmpf4YoIYgpBAFpQ8u8jW1sBafbJHPcJ/FOwrRdNNSuX3IgbBMfyH2pB\r\n7bAt1tQ1Up/oG2CvMOISKq3EjSfTem2E2LF19zY0emerIDpy9eviDv3ZUcVfWTyng0z85XKvRhKI\r\nJ1sYJBLxoEKxrmd0li8pi40alsc3zegARQ6c3MgM"}
        Map<String,String> map = new HashMap<>();
        map.put("Version","1.0.0.0");
        map.put("RequestId", "5dc416a26128d");
        map.put("Message",null);
        map.put("RequestTime","2020-11-05 14:28:50");
        map.put("Token",token);
        map.put("Sign",null);
        map = sortMapByKey(map);

        String sign = encrypt(JSON.toJSONString(map, SerializerFeature.WriteMapNullValue),appKey);
//        map.put("Sign","q0Omt8tBolPoT1CbxTgXJThfs+683dYqElc/GclJZXcozk8b1y5azndZ6H6Zm2FnjarjwqZNIblaJVKlQCa9whpSEyZmUspcLsB8QHDdo4zsrPvy0/ZbE0Om8bjQ6Cpd7DIruAQZEbJb8Sf7yJnTda+koWgedv2W2yV4ZWmEFuhJ3O8TOuCOcsdsH69ZfiYgNztWc2Iii18=");
        String replaceBlankSign = replaceBlank(sign);
        map.put("Sign",replaceBlankSign);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        ResponseEntity<String> xx = RestTemplateUtils.post("https://api.gotenchina.com/api/Product/GetCategory",headers, JSON.toJSONString(map, SerializerFeature.WriteMapNullValue), String.class);
        System.out.println(1);
    }

    @org.junit.Test
    public void parse() throws Exception {
        String json = "{\"Message\":null,\"RequestId\":\"5dc416a26128d\",\"RequestTime\":\"2020-11-05 14:28:50\",\"Sign\":null,\"Token\":\"yXmV1Xf1GFXGjTK8z2x4Bw==\",\"Version\":\"1.0.0.0\"}";
        String xx = encrypt(json,"z2o7Dn8w");
        System.out.println(1);
    }

    @org.junit.Test
    public void compare(){
        String mySign = "q0Omt8tBolPoT1CbxTgXJThfs+683dYqElc/GclJZXcozk8b1y5azndZ6H6Zm2FnjarjwqZNIbla\n" +
                "JVKlQCa9whpSEyZmUspcLsB8QHDdo4zsrPvy0/ZbE0Om8bjQ6Cpd7DIruAQZEbJb8Sf7yJnTda+k\n" +
                "oWgedv2W2yV4ZWmEFuhJ3O8TOuCOcsdsH69ZfiYgNztWc2Iii18=";
        String signxx = "q0Omt8tBolPoT1CbxTgXJThfs+683dYqElc/GclJZXcozk8b1y5azndZ6H6Zm2FnjarjwqZNIblaJVKlQCa9whpSEyZmUspcLsB8QHDdo4zsrPvy0/ZbE0Om8bjQ6Cpd7DIruAQZEbJb8Sf7yJnTda+koWgedv2W2yV4ZWmEFuhJ3O8TOuCOcsdsH69ZfiYgNztWc2Iii18=";
        String xx = replaceBlank(mySign);
        System.out.println(xx.equals(signxx));
    }

}
