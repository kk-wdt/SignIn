package com.kktt.jesus.schedule;

import com.alibaba.fastjson.JSON;
import com.kktt.jesus.TaskConsumerComponent;
import com.kktt.jesus.dao.source1.AliexpressSkuPublishDao;
import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import com.kktt.jesus.service.RedisQueueService;
import com.kktt.jesus.utils.FileUtil;
import com.kktt.jesus.utils.ImgTools;
import com.kktt.jesus.utils.OssUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class AliSyncImageScheduler {
    protected static final Logger logger = LoggerFactory.getLogger(AliSyncImageScheduler.class);
    public static final String DP_QUEUE_ALI_IMAGE_CHANGE_TASK = "DP_Queue_Ali_Image_Change_Task";
    private static final String UPLOAD_PREFIX_URL = "https://gotenimh.oss-cn-hangzhou.aliyuncs.com/";

    @Resource
    private AliexpressSkuPublishDao aliexpressSkuPublishDao;
    @Resource
    private RedisQueueService redisQueueService;
    @Resource
    private OssUtil ossUtil;
    @Resource
    protected TaskConsumerComponent taskConsumerComponent;

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 10 * 1000)
    public void commitTask() {
        List<AliexpressSkuPublishEntity> waitingChangeTaskList = aliexpressSkuPublishDao.queryProductIdByState(AliexpressSkuPublishEntity.STATE.UNDO);
        if(CollectionUtils.isEmpty(waitingChangeTaskList)){
            logger.info("listing图片同步 -- 暂无需要同步的图片");
            return;
        }

        List<Long> illegalProducts = new ArrayList<>();
        List<Long> legalProducts = new ArrayList<>();
        for (AliexpressSkuPublishEntity aliexpressSkuPublishEntity : waitingChangeTaskList) {
            if(!aliexpressSkuPublishEntity.getProperties().equals("{}") && StringUtils.isEmpty(aliexpressSkuPublishEntity.getSkuImageUrl())){
                //不合适的商品
                illegalProducts.add(aliexpressSkuPublishEntity.getProductId());
            }else{
                legalProducts.add(aliexpressSkuPublishEntity.getProductId());
            }
        }

        //更新状态
        if(CollectionUtils.isNotEmpty(legalProducts)){
            aliexpressSkuPublishDao.updateUndoStateByProductId(legalProducts,AliexpressSkuPublishEntity.STATE.DOING);
            //扔到redis 一个个做图片下载和转化
            List<String> productIds = legalProducts.stream().map(Object::toString).collect(Collectors.toList());
            redisQueueService.push(DP_QUEUE_ALI_IMAGE_CHANGE_TASK,productIds);
        }
        if(CollectionUtils.isNotEmpty(illegalProducts)){
            aliexpressSkuPublishDao.updateUndoStateByProductId(illegalProducts,AliexpressSkuPublishEntity.STATE.IGNORE);
        }
        logger.info("listing图片同步 -- 同步开始 数量：{}",legalProducts.size());
    }

    @Scheduled(fixedDelay = 10 * 1000, initialDelay = 10 * 1000)
    public void fetchTask() {
        fetchImage();
    }

    @Scheduled(fixedDelay = 10 * 1000, initialDelay = 10 * 1000)
    public void fetchTask1() {
        fetchImage();
    }

    @Scheduled(fixedDelay = 10 * 1000, initialDelay = 10 * 1000)
    public void fetchTask2() {
        fetchImage();
    }

    private void fetchImage(){
        taskConsumerComponent.consume(DP_QUEUE_ALI_IMAGE_CHANGE_TASK, productId -> {
            if(StringUtils.isEmpty(productId)){
                return;
            }
            List<AliexpressSkuPublishEntity> productItems = aliexpressSkuPublishDao.querySyncImage(Long.parseLong(productId));
            if(CollectionUtils.isEmpty(productItems)){
                return;
            }
            replaceAliexpressImage(productItems);
        });
    }

    private String changeImage(String url)  {
        if(StringUtils.isEmpty(url) || url.startsWith("https://dp-products-upload")){
            return url;
        }
        String imagePath = FileUtil.getTempImagePath();
        try {
            //修改图片
            ImgTools.scaleImage(url,imagePath);
            //上传S3
            String fileName = String.format("%s.jpg", UUID.randomUUID().toString());
            File file = new File(imagePath);
            ossUtil.upload(file,fileName);
            return UPLOAD_PREFIX_URL + fileName;
        } catch (Exception e) {
            logger.error("上传图片出错:error{},image:{}",e.getMessage(),url);
            e.printStackTrace();
        }finally {
            FileUtil.deleteFile(imagePath);
        }
        return "";
    }

    public List<String> batchChangeImages(List<String> images){
        List<String> conventImageList = new ArrayList<>();
        for (String image : images) {
            conventImageList.add(changeImage(image));
        }
        return conventImageList;
    }

    private void replaceAliexpressImage(List<AliexpressSkuPublishEntity> shopNodeTaskList) {
        for (AliexpressSkuPublishEntity item : shopNodeTaskList) {
            String mainImages = item.getImageUrls();
            List<String> imageList = JSON.parseArray(mainImages, String.class);
            if(CollectionUtils.isEmpty(imageList)){
                continue;
            }
            if(imageList.get(0).startsWith("https://dp-products-upload")){
                continue;
            }
            String skuImage = item.getSkuImageUrl();
            String replaceSkuImage = changeImage(skuImage);
            item.setSkuImageUrl(replaceSkuImage);
        }
        //替换附图
        AliexpressSkuPublishEntity item = shopNodeTaskList.get(0);
        String mainImages = item.getImageUrls();
        List<String> imageList = JSON.parseArray(mainImages, String.class);
        List<String> replaceMainImages = batchChangeImages(imageList);

        for (AliexpressSkuPublishEntity child : shopNodeTaskList) {
            child.setImageUrls(JSON.toJSONString(replaceMainImages));
            child.setState(AliexpressSkuPublishEntity.STATE.FINISH_IMAGE);
        }
        logger.info("listing图片同步 -- 同步完成：productID:{}",item.getProductId());
        aliexpressSkuPublishDao.update(shopNodeTaskList);
    }

}

