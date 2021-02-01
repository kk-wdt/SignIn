package com.kktt.jesus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kktt.jesus.utils.RestTemplateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class GoTenApi extends BaseApi {

    public JSONObject getProduct() {
        Map<String, String> map = createCommonParam();
        JSONObject message = createProductRequestMessage();
        map.put("Message", message.toJSONString());
        map = sortMapByKey(map);
        String sign = encrypt(JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));
        String replaceBlankSign = replaceBlank(sign);
        map.put("Sign", replaceBlankSign);

        ResponseEntity<JSONObject> response = RestTemplateUtils.postJson(ApiConstant.PRODUCT_URL, JSON.toJSONString(map, SerializerFeature.WriteMapNullValue), JSONObject.class);
        return response.getBody();
    }

    private JSONObject createProductRequestMessage(){
        JSONObject message = new JSONObject();
        message.put("Skus", new JSONArray(Collections.singletonList("83343773")));
//        message.put("startTime","2021-01-01");
//        message.put("EndTime","2021-01-02");
        message.put("PageIndex", "1");
        message.put("Site", "www.gotenchina.com");
        return message;
    }

}


