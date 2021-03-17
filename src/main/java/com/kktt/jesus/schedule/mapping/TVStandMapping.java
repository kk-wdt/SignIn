package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TVStandMapping extends BaseMapping{

    @Override
    protected String getNodeId() {
        return "14109851";
    }

    @Override
    protected void setCategoryProperty(JSONObject property) {
        property.put("item_type","television-stands-and-entertainment-centers");
        property.put("feed_product_type","furnitureanddecor");
       // Storage Cabinets
        //设置5点描述
        List<String> bulletList = new ArrayList<>();
        bulletList.add("Modern Design Style: Cubiker L Shaped desk unifies rigid metal with MDF boards.");
        bulletList.add("Durable and Sturdy: The sturdy frame can support all your office essentials.");
        bulletList.add("Monitor Stand: removable monitor stand could be installed on either the left or right side.");
        bulletList.add("Strong Desktop: The desktop is waterproof, scratch-resistent, heat safe and designed into fine texture, beautiful and durable.");
        bulletList.add("Modern Simple Style:this computer desk delivers a peaceful experience to you wherever at home or office.");
        property.put("bullet_points",JSON.toJSONString(bulletList));

        String gk = property.getString("generic_keywords");
        String appendGk = "tv stand inch for fireplace with 70 65 entertainment console center 75 cabinet room storage stands living corner white media flat table screens wood farmhouse ";
        property.put("generic_keywords",gk + appendGk);
    }
}
