package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
       // Storage Cabinets
        //设置5点描述
        List<String> bulletList = new ArrayList<>();
        bulletList.add("It is brand new and high quality");
        bulletList.add("Ideal for Entertaining Guests Both Indoors, On the Patio, at The Bar and Outdoors");
        bulletList.add("This bar stool is rustic in feel with modern appeal, a wrap-around foot rest for comfort. It brings an effortlessly trendy look into your kitchen or dining decor.");
        bulletList.add("Modern design with clean, precise lines, Our Barstool Set is the ideal modern accessory for your interior spac");
        bulletList.add("This bar stool is very easy to assemble within 10 mins following the instructions");
        property.put("bullet_points",JSON.toJSONString(bulletList));
    }
}
