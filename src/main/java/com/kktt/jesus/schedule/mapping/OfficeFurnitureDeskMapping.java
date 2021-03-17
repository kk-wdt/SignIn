package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OfficeFurnitureDeskMapping extends BaseMapping{

    @Override
    protected String getNodeId() {
        return "3733671";
    }

    @Override
    protected void setCategoryProperty(JSONObject property) {
        property.put("item_type","home-office-desks");
        property.put("feed_product_type","desk");
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
        String appendGk = "desk white small desks for computer table office gaming black home spaces simple writing deak bedroom modern chair space tables large metal cheap study dwsk dollars whit laptop long school plain antique 31 a studying wide work dorm room bedrooms ";
        property.put("generic_keywords",gk + appendGk);

    }
}
