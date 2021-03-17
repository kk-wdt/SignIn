package com.kktt.jesus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kktt.jesus.api.GoTenApi;
import com.kktt.jesus.api.ProductConverter;
import com.kktt.jesus.dao.source1.GotenProductDao;
import com.kktt.jesus.dao.source1.PublishMapper;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.dataobject.CommonEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import com.kktt.jesus.schedule.GoTenProductSyncScheduler;
import com.kktt.jesus.service.RedisQueueService;
import com.kktt.jesus.utils.CommonUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.omg.PortableServer.POA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisServer;
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
        property.put("fulfillment_latency","5");
        property.put("feed_product_type","outdoorliving");

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
        GotenProduct xx = productConverter.getProduct(93152635L);
        System.out.println(1);
    }

    @org.junit.Test
    public void warehouse(){
        GoTenApi api = new GoTenApi();
        com.alibaba.fastjson.JSONObject ss = api.getWarehouse();
        System.out.println(1);
    }

    @org.junit.Test
    public void logistics(){
        GoTenApi api = new GoTenApi();
        com.alibaba.fastjson.JSONObject ss = api.getLogistics();
        System.out.println(1);
    }

    @org.junit.Test
    public void delete(){
        //删除无用的商品
        List<GotenProduct> all = gotenProductDao.queryAll();
        for (GotenProduct gotenProduct : all) {
            GotenProduct product = productConverter.getProduct(gotenProduct.getSku());
            if(product == null){
                gotenProductDao.batchUpdateState(Collections.singletonList(gotenProduct.getId()),GotenProduct.STATE.DELETE);
            }
        }
    }

    @org.junit.Test
    public void testPrice(){
        Map<Long, BigDecimal> xx = productConverter.getProductPrice(Collections.singletonList(70118289L));
        System.out.println(1);
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

    protected static final Logger logger = LoggerFactory.getLogger(GoTenProductSyncScheduler.class);
    @org.junit.Test
    public void updateProduct() {
        List<GotenProduct> newProductList = gotenProductDao.queryByBulletPoint();
        if(CollectionUtils.isEmpty(newProductList)){
            return;
        }
        for (GotenProduct gotenProduct : newProductList) {
            logger.info("更新SKU:{}",gotenProduct.getSku());
            Long sku = gotenProduct.getSku();
            GotenProduct product = productConverter.getProduct(sku);
            if(product == null){
                gotenProductDao.updateState(sku+"");
                continue;
            }
            gotenProduct.setBulletPoint(product.getBulletPoint());
            gotenProduct.setProperty(product.getProperty());
            gotenProduct.setDescription(product.getDescription());
            gotenProductDao.updateByPrimaryKeySelective(gotenProduct);
        }
    }


    @org.junit.Test
    public void trans(){
        String gk = "tv\n" +
                "stand\n" +
                "inch\n" +
                "for\n" +
                "fireplace\n" +
                "with\n" +
                "70\n" +
                "65\n" +
                "entertainment\n" +
                "console\n" +
                "center\n" +
                "75\n" +
                "cabinet\n" +
                "room\n" +
                "storage\n" +
                "stands\n" +
                "living\n" +
                "corner\n" +
                "white\n" +
                "media\n" +
                "flat\n" +
                "table\n" +
                "screens\n" +
                "wood\n" +
                "farmhouse\n" +
                "in\n" +
                "electric\n" +
                "bookshelf\n" +
                "black\n" +
                "80\n" +
                "dresser\n" +
                "small\n" +
                "55\n" +
                "edison\n" +
                "heater\n" +
                "walker\n" +
                "hemnes\n" +
                "home\n" +
                "ikea\n" +
                "fire\n" +
                "doors\n" +
                "long\n" +
                "gray\n" +
                "modern\n" +
                "low\n" +
                "place\n" +
                "sala\n" +
                "ameriwood\n" +
                "under\n" +
                "de\n" +
                "furniture\n" +
                "up\n" +
                "58\n" +
                "60\n" +
                "shelves\n" +
                "rustic\n" +
                "and\n" +
                "muebles\n" +
                "ashley\n" +
                "bedroom\n" +
                "grey\n" +
                "82\n" +
                "85\n" +
                "remote\n" +
                "inches\n" +
                "90\n" +
                "solid\n" +
                "large\n" +
                "profile\n" +
                "combo\n" +
                "tvs\n" +
                "indoor\n" +
                "style\n" +
                "cherry\n" +
                "juegos\n" +
                "stanf\n" +
                "electronic\n" +
                "30\n" +
                "espresso\n" +
                "70”\n" +
                "sets\n" +
                "television\n" +
                "tb\n" +
                "component\n" +
                "la\n" +
                "sunbury\n" +
                "credenza\n" +
                "to\n" +
                "door\n" +
                "screen\n" +
                "space\n" +
                "simpli\n" +
                "high\n" +
                "theater\n" +
                "tall\n" +
                "50\n" +
                "glass\n" +
                "bench\n" +
                "70in\n" +
                "brown\n" +
                "credenzas\n" +
                "shelf\n" +
                "desk\n" +
                "game\n" +
                "units\n" +
                "para\n" +
                "extra\n" +
                "72\n" +
                "floor\n" +
                "universal\n" +
                "78\n" +
                "coffee\n" +
                "wall\n" +
                "75";

        String xx = gk.replace("\n", " ");
        System.out.println(xx);
    }
}
