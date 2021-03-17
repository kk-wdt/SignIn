package com.kktt.jesus.schedule.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AccessaryOrganizersMapping extends BaseMapping{

    @Override
    protected String getNodeId() {
        return "3744511";
    }

    @Override
    protected void setCategoryProperty(JSONObject property) {
        property.put("item_type","closet-hanging-jewelry-organizers");
        property.put("feed_product_type","jewelrystorage");
       // Storage Cabinets
        //设置5点描述
        List<String> bulletList = new ArrayList<>();
        bulletList.add("BEAUTIFULLY SIMPLE AND WONDERFULLY PRACTICAL: With multiple compartments of different sizes, this set of organization trays is designed to perfectly hold a collection of jewelry, accessories and other knickknacks. These trays can be easily split up, combined or stacked, to meet a wide variety of usage scenarios");
        bulletList.add("HANDY: For travel, business, display, home use, eyewear or jewelry storage, this display case is just the thing for keeping your valuables safe and sound.");
        bulletList.add("CLASSIFIED STORAGED: Jewelry organizer box offers protect for rings, earrings, necklaces, bracelets, hair accessories, watches and other small items.");
        bulletList.add("IDEAL GIFT: With elegant appearance, this box is perfect for personal or business use. Ideal gift for Birthday, Valentine's Day, Wedding, Anniversary, Christmas or Father's Day.");
        bulletList.add("HOME OR COMMERCIAL USE: A must for serious jewelry collectors. These make thoughtful gifts for anyone with a love of style. Function as drawer inserts or use them on dresser or shelves. It is great for commercial use in stores or trade shows as well");
        property.put("bullet_points",JSON.toJSONString(bulletList));

        String gk = property.getString("generic_keywords");
        String appendGk = " jewelry organizer tray stackable for drawer box storage drawers organizers earring trays clear jewerly dresser display small with case traveling women holder lid stacker shoe boxes tarnish anti acrylic jewlery de pin and bracelet ring jewlry jewlwey stackers earrings insert container bag joyas costume plastic valet jewely top closet cheap watch lucite organizadores chest collection jewelery collector necklace clothes wall organizador stacking pocket big jewlerly narrow jwelery edit belt squares earing gavetas rings slide lift organizor bulk 20 bags cute out organizaer rock oragnizer flat cajas jelwery y caddy hanging magic scarf prendas orgainzer jwelleries wood cosmetic wardrobe solutions gizmo felt salmon jewley jelewery square jackcubedesign safe containers cloth joyería ";
        property.put("generic_keywords",gk + appendGk);
    }
}
