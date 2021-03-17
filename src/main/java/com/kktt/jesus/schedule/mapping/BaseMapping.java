package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public abstract class BaseMapping {

    public AliexpressSkuPublishEntity convert(GotenProduct xx) throws JSONException {
        AliexpressSkuPublishEntity target = new AliexpressSkuPublishEntity();
        target.setSkuId(xx.getSku());
        target.setAmazonMarketplaceId(1);
        target.setImageUrls(xx.getImageUrls());
        target.setSkuImageUrl(xx.getSkuImageUrl());
        target.setUpdateDelete(0);
        target.setSite("us");
        target.setState(0);
        double newPrice = (xx.getPrice().floatValue()) / (0.65);
        BigDecimal tmp = new BigDecimal(newPrice+"");
        target.setPrice( tmp.setScale(2, BigDecimal.ROUND_HALF_UP));
        target.setInventory(xx.getInventory());
        target.setProperties("{}");
        target.setTitle(xx.getTitle());

        JSONObject property = new JSONObject();
        property.put("brand_name","MYKTY");
        property.put("manufacturer","MYKTY");
        property.put("description",xx.getDescription());
        property.put("generic_keywords",xx.getKeywords().replaceAll("\"","").replace("[","").replace("]"," "));
        property.put("fulfillment_latency","3");

        setCategoryProperty(property);
        if(StringUtils.isEmpty(xx.getBulletPoint()) || xx.getBulletPoint().equals("[]")){
        }else{
            property.put("bullet_points",xx.getBulletPoint());
        }

        target.setNodeValue(property.toString());
        target.setNodeId(getNodeId());
        target.setId("us-"+xx.getSku());

        target.setListingId("");
        target.setProductId(xx.getSku());
        return target;
    }

    protected abstract String getNodeId();

    protected abstract void setCategoryProperty(JSONObject property);
}
