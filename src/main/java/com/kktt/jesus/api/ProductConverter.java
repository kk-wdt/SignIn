package com.kktt.jesus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kktt.jesus.dao.source1.GotenProductDao;
import com.kktt.jesus.dataobject.GotenProduct;
import com.kktt.jesus.schedule.GoTenProductSyncScheduler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductConverter {

    @Resource
    private GoTenApi goTenApi;
    @Resource
    private GotenProductDao gotenProductDao;

    public Map<Long, BigDecimal> getProductPrice(List<Long> skuList){
        JSONObject response = goTenApi.getProductPrice(skuList);
        return parseProductPrice(skuList,response);
    }

    public Map<Long, Integer> getProductInventory(List<Long> skuList){
        JSONObject response = goTenApi.getProductInventory(skuList);
        return parseProductInventory(response);
    }

    private Map<Long,Integer> parseProductInventory(JSONObject response) {
        JSONArray messages = response.getJSONArray("Message");
        Map<Long,Integer> quantityMap = new HashMap<>();
        for (int i = 0; i < messages.size(); i++) {
            JSONObject message = messages.getJSONObject(i);
            Long sku = message.getLong("Sku");
            Integer quantity = message.getJSONArray("ProductInventorySiteList").getJSONObject(0).getJSONArray("ProductInventoryList").getJSONObject(0).getInteger("Qty");
            quantityMap.put(sku,quantity);
        }
        return quantityMap;
    }
    protected static final Logger logger = LoggerFactory.getLogger(ProductConverter.class);

    private Map<Long,BigDecimal> parseProductPrice(List<Long> skuList, JSONObject response) {
        JSONArray messages = response.getJSONArray("Message");
        if(messages == null){
            logger.error("错误的结果:{}",response.getJSONObject("Error").getString("LongMessage"));
            String skuStr = skuList.stream().map(Object::toString).collect(Collectors.joining(","));
            gotenProductDao.updateState(skuStr);
            return new HashMap<>();
        }
        Map<Long,BigDecimal> priceMap = new HashMap<>();
        for (int i = 0; i < messages.size(); i++) {
            JSONObject message = messages.getJSONObject(i);
            Long sku = message.getLong("Sku");
            JSONObject priceJson = message.getJSONArray("WarehousePriceList").getJSONObject(0);
            BigDecimal price = priceJson.getBigDecimal("SellingPrice");
            priceMap.put(sku,price);
        }
        return priceMap;
    }

    public GotenProduct getProduct(Long sku){
        JSONObject response = goTenApi.getProduct(sku);
        List<GotenProduct> result = parseProduct(response);
        return result.isEmpty()?null:result.get(0);
    }

    public int getProductSize(int index,String startDate,String endDate){
        JSONObject response = goTenApi.getProduct(index,startDate,endDate);
        if(response.getJSONObject("Message") == null){
            return 0;
        }
        return response.getJSONObject("Message").getIntValue("PageTotal");
    }

    public List<GotenProduct> getProduct(int index,String startDate,String endDate){
        JSONObject response = goTenApi.getProduct(index,startDate,endDate);
        if(response.getJSONObject("Message") == null){
            return new ArrayList<>();
        }
        return parseProduct(response);
    }

    private  List<GotenProduct>  parseProduct(JSONObject response){
        JSONArray productList = response.getJSONObject("Message").getJSONArray("ProductInfoList");

        List<GotenProduct> products = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            JSONObject productInfo = productList.getJSONObject(i);

            if(!validProduct(productInfo)) continue;

            GotenProduct gotenProduct = new GotenProduct();
            Long sku = productInfo.getLong("Sku");
            System.out.println(sku);
            gotenProduct.setSite("US");
            gotenProduct.setSku(sku);
            gotenProduct.setState((byte) 0);

            //长宽高重量
            JSONObject prop = new JSONObject();
            prop.put("SpecHeight",productInfo.getBigDecimal("SpecHeight"));
            prop.put("SpecWidth",productInfo.getBigDecimal("SpecWidth"));
            prop.put("SpecLength",productInfo.getBigDecimal("SpecLength"));
            prop.put("SpecWeight",productInfo.getBigDecimal("SpecWeight"));
            gotenProduct.setProperty(prop.toJSONString());

            gotenProduct.setCategoryFirstName(productInfo.getString("CategoryFirstNameEN"));
            gotenProduct.setCategorySecondName(productInfo.getString("CategorySecondNameEN"));
            gotenProduct.setCategoryThirdName(productInfo.getString("CategoryThirdNameEN"));
            gotenProduct.setCategoryThirdCode(productInfo.getString("CategoryThirdCode"));
            String title =productInfo.getString("EnName");
            gotenProduct.setTitle(title);

            JSONArray imageList = productInfo.getJSONArray("GoodsImageList");
            if(imageList.isEmpty()){
                continue;
            }
            List<String> extraImages = new ArrayList<>();
            for (int j = 0; j < imageList.size(); j++) {
                try{
                    JSONObject imageInfo = imageList.getJSONObject(j);
                    if(j == 0){
                        //第一张为主图
                        String mainImage = imageInfo.getString("ImageUrl");
                        gotenProduct.setSkuImageUrl(mainImage);
                    }else{
                        extraImages.add(imageInfo.getString("ImageUrl"));
                    }
                }catch (Exception e){
                    System.out.println("errorSku:"+sku);
                }
            }
            gotenProduct.setImageUrls(JSON.toJSONString(extraImages));


            //desc
            JSONArray xx = productInfo.getJSONArray("GoodsDescriptionList");
            for (int k = 0; k < xx.size(); k++) {
                JSONArray propertyArr = xx.getJSONObject(k).getJSONArray("GoodsDescriptionParagraphList");
                StringBuilder descHtml = new StringBuilder();
                for (int h = 0; h < propertyArr.size(); h++) {
                    JSONObject property = propertyArr.getJSONObject(h);
                    String desc = property.getString("GoodsDescription");
                    //最大长度2000
                    if(descHtml.length() + desc.length() <= 1500){
                        descHtml.append(desc);
                    }
                }
                gotenProduct.setDescription(descHtml.toString());
                JSONArray keywords = xx.getJSONObject(k).getJSONArray("GoodsDescriptionKeywordList");
                List<String> keywordList = new ArrayList<>();
                for (int kw = 0; kw < keywords.size(); kw++) {
                    String keyword = keywords.getJSONObject(kw).getString("KeyWord");
                    keywordList.add(keyword);
                }
                gotenProduct.setKeywords(JSONObject.toJSONString(keywordList));
            }
            gotenProduct.setCreateTime(productInfo.getSqlDate("CreateTime"));
            gotenProduct.setUpdateTime(productInfo.getSqlDate("UpdateTime"));
            products.add(gotenProduct);
        }
        return products;
    }

    private boolean validProduct(JSONObject productInfo) {
        boolean isValid = false;
        Boolean published = productInfo.getBoolean("Published");
        Boolean isProductAuth = productInfo.getBoolean("IsProductAuth");
        //是否侵权
        Boolean isTort = productInfo.getJSONObject("TortInfo").getString("TortStatus").equals("2");
        if(published && isProductAuth && isTort){
            isValid = true;
        }
        return isValid;
    }
}
