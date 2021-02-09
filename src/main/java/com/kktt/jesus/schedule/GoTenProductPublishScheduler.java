package com.kktt.jesus.schedule;

import com.kktt.jesus.TaskConsumerComponent;
import com.kktt.jesus.api.ProductConverter;
import com.kktt.jesus.dao.source1.GotenProductDao;
import com.kktt.jesus.dao.source1.PublishMapper;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import com.kktt.jesus.service.RedisQueueService;
import com.kktt.jesus.utils.CommonUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoTenProductPublishScheduler {
    protected static final Logger logger = LoggerFactory.getLogger(GoTenProductPublishScheduler.class);

    public static final String GT_SYNC_PRODUCT_PRICE_QUEUE = "UPDATE_PRICE_QUEUE";
    public static final String GT_SYNC_PRODUCT_INVENTORY_QUEUE = "UPDATE_INVENTORY_QUEUE";

    @Resource
    private GotenProductDao gotenProductDao;
    @Resource
    private ProductConverter productConverter;
    @Resource
    private RedisQueueService redisQueueService;
    @Resource
    private TaskConsumerComponent taskConsumerComponent;
    @Resource
    private PublishMapper publishMapper;

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 30 * 1000)
    public void publish() throws JSONException {
        List<GotenProduct> xx = gotenProductDao.selectValidProduct("Yard & Garden & Outdoor");

        for (GotenProduct gotenProduct : xx) {
            if(StringUtils.isEmpty(gotenProduct.getBulletPoint())){
                continue;
            }
            //根据不同类目发布到Amazon
            AliexpressSkuPublishEntity publishEntity = convert(gotenProduct);
            publishMapper.insertSelective(publishEntity);
        }
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

}