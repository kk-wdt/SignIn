package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StorageCabinetsMapping extends BaseMapping{

    @Override
    protected String getNodeId() {
        return "165119011";
    }

    @Override
    protected void setCategoryProperty(JSONObject property) {
        property.put("item_type","closet-storage-and-organization-systems");
        property.put("feed_product_type","home");
       // Storage Cabinets
        //设置5点描述
        List<String> bulletList = new ArrayList<>();
        bulletList.add("Environmental protection material:Composed of polyethylene plastic board, sturdy metal frame and ABS resin connector, the material is environmentally friendly and durable, waterproof, moisture-proof, mildew-proof and dust-proof. Just wipe with a damp cloth for daily cleaning.");
        bulletList.add("Modern aesthetic design:White door panels with simple black patterned side panels not only maintain beauty and versatility, but also keep the air unobstructed and avoid the accumulation of odors.");
        bulletList.add("Large storage performance:It can provide you with enough storage space to keep things tidy.");
        bulletList.add("Hassle-free Assembly:Simple assembly in a brief time with detailed installation manual and video provided.");
        bulletList.add("Nice Choice if you need to move your closet:An simple solution to carry around or put aside. This is gonna be exactly what you need.");
        property.put("bullet_points",JSON.toJSONString(bulletList));
    }
}
