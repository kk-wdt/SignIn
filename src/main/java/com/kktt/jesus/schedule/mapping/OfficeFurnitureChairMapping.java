package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OfficeFurnitureChairMapping extends BaseMapping{

    @Override
    protected String getNodeId() {
        return "3733721";
    }

    @Override
    protected void setCategoryProperty(JSONObject property) {
        property.put("item_type","home-office-desk-chairs");
        property.put("feed_product_type","chair");
       // Storage Cabinets
        //设置5点描述
        List<String> bulletList = new ArrayList<>();
        bulletList.add("Office chair comes with all hardware & necessary tools. Follow the desk chair instruction, you'll found easy to assemble, and computer chair estimated assembly time in about 10-15mins.");
        bulletList.add("All the accessories of our office chair have passed the test of BIFMA, which is a guarantee for your personal safety. The mesh chair can bear the weight of 250lbs.");
        bulletList.add("Sturdy guest chair provides a comfortable place to sit in your office, reception area or lobby.");
        bulletList.add("Durable metal frame for strength and reliable performance.");
        bulletList.add("An unbeatable combination of versatility and value for any environment.");
        property.put("bullet_points",JSON.toJSONString(bulletList));

        String gk = property.getString("generic_keywords");
        String appendGk = "chair office desk chairs with arms ergonomic for white home small grey adjustable mesh gray wheels tall and comfortable computer task support no clear modern lumbar big folding leather sillas flip cheap up comfy chaie xchair work desks  stylish short base\n";
        property.put("generic_keywords",gk + appendGk);
    }
}
