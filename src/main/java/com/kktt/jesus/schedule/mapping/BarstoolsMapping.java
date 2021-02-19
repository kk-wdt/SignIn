package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSONObject;

/**
 * Home & Garden /Kitchen & Dining & Bar/Barstools(GT)
 * Home & Kitchen/Furniture/Game & Recreation Room Furniture/Home Bar Furniture/Barstools(AMZ)
 */
public class BarstoolsMapping extends BaseMapping{

    @Override
    protected String getNodeId() {
        return "3733851";
    }

    @Override
    protected void setCategoryProperty(JSONObject property) {
        property.put("item_type","barstools");
        property.put("feed_product_type","stoolseating");
    }
}
