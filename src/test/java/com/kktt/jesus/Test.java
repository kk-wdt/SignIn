package com.kktt.jesus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kktt.jesus.api.ProductConverter;
import com.kktt.jesus.dao.source1.GotenProductDao;
import com.kktt.jesus.dao.source1.PublishMapper;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.dataobject.CommonEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import com.kktt.jesus.service.RedisQueueService;
import com.kktt.jesus.utils.CommonUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.omg.PortableServer.POA;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test extends BaseTest{

    @Resource
    private GotenProductDao gotenProductDao;
    @Resource
    private ProductConverter productConverter;
    @org.junit.Test
    public void test(){
        int index = 1;
        Instant now = Instant.now();
        Instant startInstant = now.minus(1, ChronoUnit.DAYS);
        String endDate = now.toString();
        String startDate = startInstant.toString();
        List<GotenProduct> xx = productConverter.getProduct(index,startDate,endDate);
        while (!CollectionUtils.isEmpty(xx)){
            saveProduct(xx);
            index ++;
            xx = productConverter.getProduct(index,startDate,endDate);
        }
    }

    @org.junit.Test
    public void updatePrice(){
        List<GotenProduct> all = gotenProductDao.selectAll();
        if(CollectionUtils.isEmpty(all)){
            return;
        }
        List<Long> skuList = all.stream().map(GotenProduct::getSku).collect(Collectors.toList());
        List<List<Long>> skuGroup = CommonUtil.subCollection(skuList, 50);
        for (List<Long> group : skuGroup) {
            Map<Long, BigDecimal> priceMap = productConverter.getProductPrice(group);
            updateProductPrice(priceMap);
        }
    }

    @org.junit.Test
    public void tess(){
        Map<Long, Integer> xx = productConverter.getProductInventory(Collections.singletonList(9988203L));
        System.out.println(1);
    }

    @org.junit.Test
    public void updateQuantity(){
        List<GotenProduct> all = gotenProductDao.selectAll();
        if(CollectionUtils.isEmpty(all)){
            return;
        }
        List<Long> skuList = all.stream().map(GotenProduct::getSku).collect(Collectors.toList());
        List<List<Long>> skuGroup = CommonUtil.subCollection(skuList, 50);
        for (List<Long> group : skuGroup) {
            Map<Long, Integer> priceMap = productConverter.getProductInventory(group);
            updateProductInventory(priceMap);
        }
    }

    @Resource
    private PublishMapper publishMapper;
    @org.junit.Test
    public void convert2Publish() throws JSONException {
        List<GotenProduct> xx = gotenProductDao.selectValidProduct("Yard & Garden & Outdoor");

        for (GotenProduct gotenProduct : xx) {
            if(StringUtils.isEmpty(gotenProduct.getBulletPoint())){
                continue;
            }
            AliexpressSkuPublishEntity publishEntity = convert(gotenProduct);
            publishMapper.insertSelective(publishEntity);
        }
        System.out.println(1);
    }

    private AliexpressSkuPublishEntity convert( GotenProduct xx) throws JSONException {
        AliexpressSkuPublishEntity target = new AliexpressSkuPublishEntity();
        target.setSkuId(xx.getSku());
        target.setAmazonMarketplaceId(1);
        target.setImageUrls(xx.getImageUrls());
        target.setSkuImageUrl(xx.getSkuImageUrl());
        target.setUpdateDelete(0);
        target.setSite("us");
        target.setState(0);
        double newPrice = (xx.getPrice().floatValue()) / (1 - 0.15 - 0.2);
        BigDecimal tmp = new BigDecimal(newPrice+"");
        target.setPrice( tmp.setScale(2, BigDecimal.ROUND_HALF_UP));
        target.setInventory(xx.getInventory());
        target.setProperties("{}");
        target.setTitle(xx.getTitle());

        JSONObject property = new JSONObject();
        property.put("brand_name","Nother");
        property.put("manufacturer","Nother");
        property.put("item_type","patio-conversation-sets");
        property.put("description",xx.getDescription());
        property.put("bullet_points",xx.getBulletPoint());
        property.put("generic_keywords",xx.getKeywords().replaceAll("\"","").replace("[","").replace("]"," "));
        property.put("fulfillment_latency","3");
        property.put("feed_product_type","outdoorliving");


//        String popt = xx.getProperty();
//        if(!StringUtils.isEmpty(popt)){
//            com.alibaba.fastjson.JSONObject popJson = JSON.parseObject(popt);
//            property.put("package_height",popJson.getString("SpecHeight"));
//            property.put("package_height_unit_of_measure","CM");
//            property.put("package_width",popJson.getString("SpecWidth"));
//            property.put("package_width_unit_of_measure","CM");
//            property.put("package_length",popJson.getString("SpecLength"));
//            property.put("package_length_unit_of_measure","CM");
//            property.put("package_weight",popJson.getString("SpecWeight"));
//            property.put("package_weight_unit_of_measure","GR");
//        }

        target.setNodeValue(property.toString());
        target.setNodeId("16135380011");
        target.setItemType("patio-conversation-sets");
        target.setId("us-"+xx.getSku());

        target.setListingId("");
        target.setProductId(xx.getSku());
        return target;
    }


    @org.junit.Test
    public void getSingleProduct(){
        GotenProduct x = find(61650971L);
        GotenProduct xx = productConverter.getProduct(71549874L);
    }

    private GotenProduct find(Long sku){
        Example example = new Example(GotenProduct.class);
        example.createCriteria().andEqualTo("sku", sku);
        GotenProduct xx = gotenProductDao.selectOneByExample(example);
        return xx;
    }

    private void saveProduct(List<GotenProduct> xx ){
        for (GotenProduct product : xx) {
            gotenProductDao.insertIgnore(product);
        }
    }

    private void updateProductInventory(Map<Long,Integer> inventoryMap) {
        List<Map<String,Object>> datas = new ArrayList<>();
        inventoryMap.forEach((k,v)->{
            Map<String,Object> item = new HashMap<>();
            item.put("sku",k);
            item.put("inventory",v);
            datas.add(item);
        });
        gotenProductDao.batchUpdateInventory(datas);
    }

    private void updateProductPrice(Map<Long,BigDecimal> priceMap) {
        List<Map<String,Object>> datas = new ArrayList<>();
        priceMap.forEach((k,v)->{
            Map<String,Object> item = new HashMap<>();
            item.put("sku",k);
            item.put("price",v);
            datas.add(item);
        });
        gotenProductDao.batchUpdatePrice(datas);
    }

}
