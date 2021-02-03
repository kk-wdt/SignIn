package com.kktt.jesus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kktt.jesus.dataobject.GotenProductEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductConverter {

    @Resource
    private GoTenApi goTenApi;

    public List<GotenProductEntity> getProduct(){
        JSONObject response = goTenApi.getProduct();
        JSONArray productList = response.getJSONObject("Message").getJSONArray("ProductInfoList");


        List<GotenProductEntity> products = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            JSONObject productInfo = productList.getJSONObject(i);
            GotenProductEntity gotenProductEntity = new GotenProductEntity();
            Long sku = productInfo.getLong("Sku");
            gotenProductEntity.setSite("US");
            gotenProductEntity.setSku(sku);
            gotenProductEntity.setState((byte) 4);

            gotenProductEntity.setCategoryFirstName(productInfo.getString("CategoryFirstNameEN"));
            gotenProductEntity.setCategorySecondName(productInfo.getString("CategorySecondNameEN"));
            gotenProductEntity.setCategoryThirdName(productInfo.getString("CategoryThirdNameEN"));
            gotenProductEntity.setCategoryThirdCode(productInfo.getString("CategoryThirdCode"));
            String title =productInfo.getString("EnName");
            gotenProductEntity.setTitle(title);

            JSONArray imageList = productInfo.getJSONArray("GoodsImageList");
            List<String> extraImages = new ArrayList<>();
            for (int j = 0; j < imageList.size(); j++) {
                JSONObject imageInfo = imageList.getJSONObject(i);
                if(j == 0){
                    //第一张为主图
                    String mainImage = imageInfo.getString("ImageUrl");
                    gotenProductEntity.setSkuImageUrl(mainImage);
                }else{
                    extraImages.add(imageInfo.getString("ImageUrl"));
                }
            }
            gotenProductEntity.setImageUrls(JSON.toJSONString(extraImages));


            //desc
            JSONArray xx = productInfo.getJSONArray("GoodsDescriptionList");
            for (int k = 0; k < xx.size(); k++) {
                JSONArray propertyArr = xx.getJSONObject(k).getJSONArray("GoodsDescriptionParagraphList");
                for (int h = 0; h < propertyArr.size(); h++) {
                    JSONObject property = propertyArr.getJSONObject(h);
                    String propertyName = property.getString("ParagraphName");
                    if(propertyName.equalsIgnoreCase("规格")){
                        gotenProductEntity.setDescription(property.getString("GoodsDescription"));
                    };
//                if(propertyName.equalsIgnoreCase("包装内含")){
//                    gotenProductEntity.setDescription(property.getString("GoodsDescription"));
//                };
//
//                if(propertyName.equalsIgnoreCase("首段描述")){
//                    gotenProductEntity.setDescription(property.getString("GoodsDescription"));
//                };
                    if(propertyName.equalsIgnoreCase("特征")){
                        gotenProductEntity.setBulletPoint(property.getString("GoodsDescription"));
                    };
                }
                JSONArray keywords = xx.getJSONObject(k).getJSONArray("GoodsDescriptionKeywordList");
                List<String> keywordList = new ArrayList<>();
                for (int kw = 0; kw < keywords.size(); kw++) {
                    String keyword = keywords.getJSONObject(kw).getString("KeyWord");
                    keywordList.add(keyword);
                }
                gotenProductEntity.setKeywords(JSONObject.toJSONString(keywordList));
            }
            gotenProductEntity.setCreateTime(productInfo.getSqlDate("CreateTime"));
            gotenProductEntity.setUpdateTime(productInfo.getSqlDate("UpdateTime"));
            products.add(gotenProductEntity);
        }
        return products;
    }
}
