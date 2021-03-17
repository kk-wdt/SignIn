package com.kktt.jesus.schedule;

import com.alibaba.fastjson.JSON;
import com.kktt.jesus.api.ProductConverter;
import com.kktt.jesus.dao.source1.AliexpressSkuPublishDao;
import com.kktt.jesus.dao.source1.PublishMapper;
import com.kktt.jesus.dataobject.AliExpressItem;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import com.kktt.jesus.schedule.task.AliExpressListTask;
import com.kktt.jesus.service.RedisQueueService;
import com.kktt.jesus.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kktt.jesus.schedule.AmzListingManageScheduler.GT_QUEUE_FEEDS_REQUEST_TASK;

@Component
public class GoTenProductMonitorScheduler {
    protected static final Logger logger = LoggerFactory.getLogger(GoTenProductMonitorScheduler.class);

    @Resource
    private PublishMapper publishMapper;
    @Resource
    private ProductConverter productConverter;
    @Resource
    private RedisQueueService redisQueueService;
    @Resource
    private AliexpressSkuPublishDao aliexpressSkuPublishDao;

//    @Scheduled(fixedDelay = 1000 * 1000, initialDelay = 10 * 1000)
    public void deleteErrorProduct(){
        Example example = new Example(AliexpressSkuPublishEntity.class);
        example.createCriteria().andEqualTo("state", 2);
        List<AliexpressSkuPublishEntity> monitorData = publishMapper.selectByExample(example);

        List<AliexpressSkuPublishEntity> updateList =new ArrayList<>();

        List<AliExpressItem> inventoryUpdateList = new ArrayList<>();
        for (AliexpressSkuPublishEntity monitorDatum : monitorData) {
            Long sku = monitorDatum.getSkuId();
            GotenProduct product = productConverter.getProduct(sku);
            if(product == null){
                inventoryUpdateList.add(new AliExpressItem(getSku(sku),0));
                updateList.add(monitorDatum);
            }
        }
        if(CollectionUtils.isNotEmpty(inventoryUpdateList)){
            List<String> skuList = updateList.stream().map(AliexpressSkuPublishEntity::getId).collect(Collectors.toList());
            aliexpressSkuPublishDao.updateState(skuList,AliexpressSkuPublishEntity.STATE.IGNORE);
            logger.info("速卖通价格 - 库存监听:删除：{}",inventoryUpdateList.size());
            pushTask(1,AliExpressListTask.TYPE.INVENTORY,inventoryUpdateList);
        }

    }


    @Scheduled(fixedDelay = 1000 * 1000, initialDelay = 10 * 1000)
//    @Scheduled(cron = "0 10 0 * * ?", zone = "GMT+8")
    public void runSyncProduct() {
        Example example = new Example(AliexpressSkuPublishEntity.class);
        example.createCriteria().andEqualTo("state", 2);
        List<AliexpressSkuPublishEntity> monitorData = publishMapper.selectByExample(example);


        //监听库存和价格
        List<List<AliexpressSkuPublishEntity>> group = CommonUtil.subCollection(monitorData, 50);

        List<AliExpressItem> priceUpdateList = new ArrayList<>();
        List<AliExpressItem> inventoryUpdateList = new ArrayList<>();
        List<AliexpressSkuPublishEntity> updateList = new ArrayList<>();

        for (List<AliexpressSkuPublishEntity> groupData : group) {
            //监听库存
            List<Long> groupSkuList = groupData.stream().map(AliexpressSkuPublishEntity::getSkuId).collect(Collectors.toList());
            Map<Long, Integer> inventoryMap = productConverter.getProductInventory(groupSkuList);
            Map<Long, BigDecimal> priceMap = productConverter.getProductPrice(groupSkuList);

            for (AliexpressSkuPublishEntity oldData : groupData) {
                //判断库存是否为0或者商品找不到
                Long sku = oldData.getSkuId();
                Integer inventory = inventoryMap.get(sku);
                BigDecimal originPrice = priceMap.get(sku);
                boolean isChange = false;
                if(inventory == null || originPrice == null){
                    //商品不存在 也删除
                    inventoryUpdateList.add(new AliExpressItem(getSku(sku),0));
                    oldData.setInventory(0);
                    isChange = true;
                }else{
                    BigDecimal currentSalePrice = recalculatePrice(originPrice);
                    if(oldData.getPrice().compareTo(currentSalePrice) != 0){
                        oldData.setPrice(currentSalePrice);
                        priceUpdateList.add(new AliExpressItem(getSku(sku),currentSalePrice.doubleValue()));
                        isChange = true;
                    }

                    if(inventory.compareTo(oldData.getInventory()) != 0){
                        oldData.setInventory(inventory);
                        inventoryUpdateList.add(new AliExpressItem(getSku(sku),inventory));
                        isChange = true;
                    }
                }
                if(isChange){
                    updateList.add(oldData);
                }
            }

        }
        if(CollectionUtils.isNotEmpty(priceUpdateList)){
            logger.info("速卖通价格 - 库存监听:价格更新数量：{}",priceUpdateList.size());
            pushTask(1,AliExpressListTask.TYPE.PRICE,priceUpdateList);
        }
        if(CollectionUtils.isNotEmpty(inventoryUpdateList)){
            logger.info("速卖通价格 - 库存监听:库存更新数量：{}",inventoryUpdateList.size());
            pushTask(1,AliExpressListTask.TYPE.INVENTORY,inventoryUpdateList);
        }
        if(CollectionUtils.isNotEmpty(updateList)){
            logger.info("速卖通价格 - 库存监听:总更新数量：{}",updateList.size());
            publishMapper.batchUpdate(updateList);
        }else {
            logger.info("速卖通价格 - 库存监听: 没有数据发生变化");
        }

    }

    private String getSku(Long skuId){
        return "gt_s1_"+skuId;
    }

    private BigDecimal recalculatePrice(BigDecimal originPrice){
        BigDecimal tmp  = originPrice.divide(new BigDecimal("0.65"),BigDecimal.ROUND_HALF_UP);
        return tmp.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private void pushTask(Integer amazonMarketplaceId,byte type,List<AliExpressItem> items){
        String uuid = UUID.randomUUID().toString();
        AliExpressListTask aliExpressListTask = new AliExpressListTask();
        aliExpressListTask.setId(amazonMarketplaceId);
        aliExpressListTask.setTasks(items);
        aliExpressListTask.setType(type);
        aliExpressListTask.setUuid(uuid);
        redisQueueService.push(GT_QUEUE_FEEDS_REQUEST_TASK, JSON.toJSONString(aliExpressListTask));
    }



}