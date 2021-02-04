package com.kktt.jesus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kktt.jesus.utils.RestTemplateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class GoTenApi extends BaseApi {

    public JSONObject getProductPrice(List<Long> skuList){
        Map<String, String> map = createCommonParam();
        JSONObject message =  createCommonProductRequestMessage(skuList);
        map.put("Message", message.toJSONString());
        map = sortMapByKey(map);
        String sign = encrypt(JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));
        String replaceBlankSign = replaceBlank(sign);
        map.put("Sign", replaceBlankSign);
        ResponseEntity<JSONObject> response = RestTemplateUtils.postJson(ApiConstant.PRODUCT_PRICE_URL, JSON.toJSONString(map, SerializerFeature.WriteMapNullValue), JSONObject.class);
        return response.getBody();
    }

    public JSONObject getProductInventory(List<Long> skuList){
        Map<String, String> map = createCommonParam();
        JSONObject message =  createCommonProductRequestMessage(skuList);
        map.put("Message", message.toJSONString());
        map = sortMapByKey(map);
        String sign = encrypt(JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));
        String replaceBlankSign = replaceBlank(sign);
        map.put("Sign", replaceBlankSign);
        ResponseEntity<JSONObject> response = RestTemplateUtils.postJson(ApiConstant.PRODUCT_INVENTORY_URL, JSON.toJSONString(map, SerializerFeature.WriteMapNullValue), JSONObject.class);
        return response.getBody();
    }

    public JSONObject getProduct(Long sku) {
        Map<String, String> map = createCommonParam();
        JSONObject message =  createProductRequestMessage(Collections.singletonList(sku));
        map.put("Message", message.toJSONString());
        map = sortMapByKey(map);
        String sign = encrypt(JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));
        String replaceBlankSign = replaceBlank(sign);
        map.put("Sign", replaceBlankSign);

        ResponseEntity<JSONObject> response = RestTemplateUtils.postJson(ApiConstant.PRODUCT_URL, JSON.toJSONString(map, SerializerFeature.WriteMapNullValue), JSONObject.class);
        return response.getBody();
    }

    public JSONObject getProduct(int index,String startDate,String endDate) {
        Map<String, String> map = createCommonParam();
        JSONObject message = createProductRequestMessage(index,startDate,endDate);
        map.put("Message", message.toJSONString());
        map = sortMapByKey(map);
        String sign = encrypt(JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));
        String replaceBlankSign = replaceBlank(sign);
        map.put("Sign", replaceBlankSign);

        ResponseEntity<JSONObject> response = RestTemplateUtils.postJson(ApiConstant.PRODUCT_URL, JSON.toJSONString(map, SerializerFeature.WriteMapNullValue), JSONObject.class);
        return response.getBody();
    }

    private JSONObject createProductRequestMessage(int index,String startDate,String endDate){
        JSONObject message = new JSONObject();
        message.put("startTime",startDate);
        message.put("EndTime",endDate);
        message.put("PageIndex", index);
        message.put("Site", "www.gotenchina.com");
        return message;
    }

    private JSONObject createProductRequestMessage(List<Long> skuList){
        JSONObject message = new JSONObject();
        message.put("Skus",JSON.parseArray(JSONObject.toJSONString(skuList)));
        message.put("Site", "www.gotenchina.com");
        return message;
    }

    private JSONObject createCommonProductRequestMessage(List<Long> skuList){
        JSONObject message = new JSONObject();
        message.put("SkuList",JSON.parseArray(JSONObject.toJSONString(skuList)));
        message.put("Site", "www.gotenchina.com");
        return message;
    }
}


