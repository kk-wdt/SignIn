package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.dataobject.GotenProduct;

import java.math.BigDecimal;

public abstract class BaseMapping {

    protected AliexpressSkuPublishEntity convert(GotenProduct xx) throws JSONException {
        AliexpressSkuPublishEntity target = new AliexpressSkuPublishEntity();
        target.setSkuId(xx.getSku());
        target.setAmazonMarketplaceId(1);
        target.setImageUrls(xx.getImageUrls());
        target.setSkuImageUrl(xx.getSkuImageUrl());
        target.setUpdateDelete(0);
        target.setSite("us");
        target.setState(0);
        double newPrice = (xx.getPrice().floatValue()) / (1 - 0.15 - 0.2);
        BigDecimal tmp = new BigDecimal(newPrice+"");
        target.setPrice( tmp.setScale(2, BigDecimal.ROUND_HALF_UP));
        target.setInventory(xx.getInventory());
        target.setProperties("{}");
        target.setTitle(xx.getTitle());

        JSONObject property = new JSONObject();
        property.put("brand_name","Nother");
        property.put("manufacturer","Nother");
        property.put("description",xx.getDescription());
        property.put("bullet_points",xx.getBulletPoint());
        property.put("generic_keywords",xx.getKeywords().replaceAll("\"","").replace("[","").replace("]"," "));
        property.put("fulfillment_latency","3");
        property.put("item_type","patio-conversation-sets");
        property.put("feed_product_type","outdoorliving");

        target.setNodeValue(property.toString());
        target.setNodeId("16135380011");
        target.setItemType("patio-conversation-sets");
        target.setId("us-"+xx.getSku());

        target.setListingId("");
        target.setProductId(xx.getSku());
        return target;
    }
}
