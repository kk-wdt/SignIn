package com.kktt.jesus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.*;

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
        message.put("Skus",new JSONArray(Collections.singletonList("83343773")));
//        message.put("startTime","2021-01-01");
//        message.put("EndTime","2021-01-02");
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
        productInfo.getString("");
        System.out.println(1);


        AliexpressSkuPublishEntity entity = new AliexpressSkuPublishEntity();
        Long sku = productInfo.getLong("Sku");
        entity.setSkuId(sku);
        entity.setProductId(sku);
        entity.setSite("us");
        entity.setAmazonMarketplaceId(1);
        entity.setState(AliexpressSkuPublishEntity.STATE.FINISH_IMAGE);
        String title =productInfo.getString("EnName");
        entity.setTitle(title);
        entity.setProperties("{}");
        entity.setNodeId("");

        String nodeValye = createNodeValue(productInfo);




        //获取图片
        JSONArray imageList = productInfo.getJSONArray("GoodsImageList");
        List<String> extraImages = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            JSONObject imageInfo = imageList.getJSONObject(i);
            if(i == 0){
                //第一张为主图
                String mainImage = imageInfo.getString(imageInfo.getString("ImageUrl"));
                entity.setSkuImageUrl(mainImage);
            }else{
                extraImages.add(imageInfo.getString(imageInfo.getString("ImageUrl")));
            }
        }
        entity.setImageUrls(JSON.toJSONString(extraImages));


    }

    //{"brand_name": "MEGNATIA", "description": "Perfect and appropriate size for your best coffee or tea drinking experience or cold drinks,it's up to you.", "bullet_points": ["NOVELTY COFFEE MUG:The most fashion travel mug is simple,funny and backed with a lot of love.You are in a pleasure mood when you drink water,milk,coffee and juice with it.", "MULTIPLE USAGE:Not just for coffee, our novelty coffee mug can also be used as a paperweight, storage for pens or loose change, droid parts, or even mug cakes!", "MAKE A PERFECT GIFT:Great gifts for women,men,children,friends,family. The cute mugs form a set suitable to bring out on special occasions for a memorable time.It's also a good present for halloween,christmas,holidays, birthdays, wedding anniversaries, graduation and more!", "SATISFIED CUSTOMER SERVICE:We will pack the mug with a durable box  and deliver it to you safely. If you have any questions when you receive the mug, please contact us by email immediately.", "NOTE:Not suitable for microwave or dishwasher.If you want to personalized the coffee mugs,please contact us."], "generic_keywords": "Coffee Mugs Travel  Tea Cup Tumbler Yeti Trump Fathers Day Gifts Bob Ross Ember Drinkware Xmas Teaware Handle Coffeeware Customizable Diy Papa", "feed_product_type": "drinkingcup", "fulfillment_latency": "3", "recommended_browse_nodes": "7953143051"}
    private String createNodeValue(JSONObject productInfo) {
        JSONObject obj  = new JSONObject();
        obj.put("brand_name","N/C");
        obj.put("description","N/C");

        return obj.toJSONString();
    }

}
