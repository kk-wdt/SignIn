package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Home & Garden /Kitchen & Dining & Bar/Barstools(GT)
 * Home & Kitchen/Furniture/Game & Recreation Room Furniture/Home Bar Furniture/Barstools(AMZ)
 */
public class StorageCabinetsMapping extends BaseMapping{

    @Override
    protected String getNodeId() {
        return "4975784051";
    }

    @Override
    protected void setCategoryProperty(JSONObject property) {
        property.put("item_type","Utility Shelves");
        property.put("feed_product_type","stoolseating");
       // Storage Cabinets
        //设置5点描述
        List<String> bulletList = new ArrayList<>();
        bulletList.add("");
        bulletList.add("");
        bulletList.add("");
        bulletList.add("");
        bulletList.add("");
        property.put("bullet_points",JSON.toJSONString(bulletList));
    }
}
