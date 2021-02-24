package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UtilityShelvesMapping extends BaseMapping{

    @Override
    protected String getNodeId() {
        return "13400741";
    }

    @Override
    protected void setCategoryProperty(JSONObject property) {
        property.put("item_type","tool-utility-shelves");
        property.put("feed_product_type","shelf");
       // Storage Cabinets
        //设置5点描述
        List<String> bulletList = new ArrayList<>();
        bulletList.add("New and in good condition");
        bulletList.add("High quality, solid structure and strong hardness");
        bulletList.add("Provide versatile storage in compact spaces to keep supplies and essentials neatly stored in your kitchen office garage");
        bulletList.add("No tools required for assembly, super simple installation requires less than 30 minutes of your time.");
        bulletList.add("Brings garage, basement or kitchen storage ideas to life");
        property.put("bullet_points",JSON.toJSONString(bulletList));
    }
}
