package com.kktt.jesus.schedule;

import com.alibaba.fastjson.JSONException;
import com.kktt.jesus.dao.source1.GotenProductDao;
import com.kktt.jesus.dao.source1.PublishMapper;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import com.kktt.jesus.schedule.mapping.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 10 * 1000)
    public void publishStorageCabinets() throws JSONException {
        publish("Storage Cabinets");
    }

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 10 * 1000)
    public void publishPatioGardenFurniture() throws JSONException {
        publish("Patio & Garden Furniture");
    }

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 10 * 1000)
    public void publishDesk() throws JSONException {
        publish("Desk","Office Furniture");
        publish("Desk","Dining Tables & Chairs");
    }

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 10 * 1000)
    public void publishChair() throws JSONException {
        publish("Chair","Office Furniture");
        publish("Chair","Dining Tables & Chairs");
    }

//    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 10 * 1000)
//    public void publishAO() throws JSONException {
//        publish("Accessory Organizers");
//    }

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 10 * 1000)
    public void publishTVStand() throws JSONException {
        publish("Side Cabinets & TV Stands");
    }

    private void publish(String category){
        publish(category,"");
    }

    private void publish(String category,String thirdCategory){
        logger.info("开始发布新品：类目：{}",category);
        List<GotenProduct> xx;
        if (category.equals("Chair") || category.equals("Desk")) {
            xx = gotenProductDao.selectDeskChair(category,thirdCategory);
        }else{
            xx = gotenProductDao.selectValidProduct(category);
        }

        if(CollectionUtils.isEmpty(xx)){
            logger.info("开始发布新品 无商品 跳过：类目：{}",category);
            return;
        }
        logger.info("开始发布新品 类目：{} -- 数量：{}",category,xx.size());
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
                case "Storage Cabinets":
                    publishEntity = new StorageCabinetsMapping().convert(gotenProduct);
                    break;
                case "Patio & Garden Furniture":
                    publishEntity = new PatioGardenFurnitureMapping().convert(gotenProduct);
                    break;
                case "Desk":
                    publishEntity = new OfficeFurnitureDeskMapping().convert(gotenProduct);
                    break;
                case "Chair":
                    publishEntity = new OfficeFurnitureChairMapping().convert(gotenProduct);
                    break;
                case "Accessory Organizers":
                    publishEntity = new AccessaryOrganizersMapping().convert(gotenProduct);
                    break;
                case "Side Cabinets & TV Stands":
                    publishEntity = new TVStandMapping().convert(gotenProduct);
                    break;
            }
            AliexpressSkuPublishEntity exist = publishMapper.selectByPrimaryKey(publishEntity.getId());
            if(exist == null){
                publishMapper.insertSelective(publishEntity);
            }
        }
    }

}