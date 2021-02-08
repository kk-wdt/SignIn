package com.kktt.jesus.schedule;

import com.kktt.jesus.api.ProductConverter;
import com.kktt.jesus.dao.source1.PublishMapper;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import com.kktt.jesus.utils.CommonUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoTenProductScheduler {

    @Resource
    private PublishMapper publishMapper;
    @Resource
    private ProductConverter productConverter;

    @Scheduled(fixedDelay = 10 * 1000, initialDelay = 10 * 1000)
//    @Scheduled(cron = "0 10 0 * * ?", zone = "GMT+8")
    public void runSyncProduct() {
        Example example = new Example(AliexpressSkuPublishEntity.class);
        example.createCriteria().andEqualTo("state", 2);
        List<AliexpressSkuPublishEntity> monitorData = publishMapper.selectByExample(example);
        //监听库存和价格

        List<List<AliexpressSkuPublishEntity>> group = CommonUtil.subCollection(monitorData, 50);
        for (List<AliexpressSkuPublishEntity> groupData : group) {
            //监听库存
            List<Long> groupSkuList = groupData.stream().map(AliexpressSkuPublishEntity::getSkuId).collect(Collectors.toList());
            Map<Long, Integer> inventoryMap = productConverter.getProductInventory(groupSkuList);
            for (AliexpressSkuPublishEntity oldData : groupData) {
                //判断库存是否为0或者商品找不到
                Long sku = oldData.getSkuId();
                Integer inventory = inventoryMap.get(sku);
                //请求MWS 更新库存
                if(inventory == null){
                    //商品不存在 也删除
                    continue;
                }
                
            }


        }

    }






}