package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatioGardenFurnitureMapping extends BaseMapping{

    @Override
    protected String getNodeId() {
        return "16135380011";
    }

    @Override
    protected void setCategoryProperty(JSONObject property) {
        property.put("item_type","patio-conversation-sets");
        property.put("feed_product_type","outdoorliving");
       // Storage Cabinets
        //设置5点描述
        List<String> bulletList = new ArrayList<>();
        bulletList.add("Comfortable&Convenient: It is light, so the set whole weight is also light, you can move it to any suitable place easily.");
        bulletList.add("Widely Used: The combination design allows the set can be used in many occasions such poolside, backyard or garden.");
        bulletList.add("Easy & Quick Assembly: Can complete assembly in 20 minutes, easy to keep clean, never worry to split, crack and fade. Good choice for patio, porch, backyard, balcony, poolside, garden and other suitable space in your home, which perfect for indoor & outdoor using and meet the purpose of decorating the leisure places you need.");
        bulletList.add("Perfect for small Balconies: This conversation set combines the elegance with simplicity. Thanks to the compact size, the bistro set allows you to taste the peace and harmony with your loved ones even in small balcony.");
        bulletList.add("IMPORTANT: It has a long service time, durable in use.");
        property.put("bullet_points",JSON.toJSONString(bulletList));

        String gk = property.getString("generic_keywords");
        String appendGk = " wicker loveseat outdoor patio chairs table set bench chair cushions and backyard sets for greesum back rattan balcony with 4 furniture of indoor clearance ";
        property.put("generic_keywords",gk + appendGk);
    }
}
