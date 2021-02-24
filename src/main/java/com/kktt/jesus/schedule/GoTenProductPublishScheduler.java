package com.kktt.jesus.schedule;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kktt.jesus.TaskConsumerComponent;
import com.kktt.jesus.api.ProductConverter;
import com.kktt.jesus.dao.source1.GotenProductDao;
import com.kktt.jesus.dao.source1.PublishMapper;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import com.kktt.jesus.schedule.mapping.BarstoolsMapping;
import com.kktt.jesus.schedule.mapping.UtilityShelvesMapping;
import com.kktt.jesus.service.RedisQueueService;
import com.kktt.jesus.utils.CommonUtil;
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
    private PublishMapper publishMapper;

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 10 * 1000)
    public void publishBarsTools() throws JSONException {
        publish("Barstools");
    }

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 10 * 1000)
    public void publishUtilityShelves() throws JSONException {
        publish("Utility Shelves");
    }

    private void publish(String category){
        logger.info("开始发布新品：类目：{}",category);
        List<GotenProduct> xx = gotenProductDao.selectValidProduct(category);
        if(CollectionUtils.isEmpty(xx)){
            logger.info("开始发布新品 无商品 跳过：类目：{}",category);
            return;
        }
        List<String> idList = xx.stream().map(GotenProduct::getId).collect(Collectors.toList());
        //更新为已发布过
        gotenProductDao.batchUpdateState(idList,GotenProduct.STATE.PUBLISHED);
        for (GotenProduct gotenProduct : xx) {
            if(StringUtils.isEmpty(gotenProduct.getBulletPoint())){
                continue;
            }
            //根据不同类目发布到Amazon
            AliexpressSkuPublishEntity publishEntity = null;
            switch (category){
                case "Utility Shelves" :
                    publishEntity = new UtilityShelvesMapping().convert(gotenProduct);
                    break;
                case "Barstools":
                    publishEntity = new BarstoolsMapping().convert(gotenProduct);
                    break;
            }
            AliexpressSkuPublishEntity exist = publishMapper.selectByPrimaryKey(publishEntity.getId());
            if(exist == null){
                publishMapper.insertSelective(publishEntity);
            }
        }
    }

}