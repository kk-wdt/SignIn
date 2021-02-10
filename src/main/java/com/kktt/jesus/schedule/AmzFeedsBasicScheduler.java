package com.kktt.jesus.schedule;

import com.alibaba.fastjson.JSON;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.FeedSubmissionInfo;
import com.amazonaws.mws.model.GetFeedSubmissionListResponse;
import com.amazonaws.mws.model.GetFeedSubmissionListResult;
import com.kktt.jesus.TaskConsumerComponent;
import com.kktt.jesus.dataobject.AmazonMarketplace;
import com.kktt.jesus.dataobject.ListingCreateRecordsEntity;
import com.kktt.jesus.exception.MWSRequestException;
import com.kktt.jesus.exception.MWSRequestThrottledException;
import com.kktt.jesus.mws.MwsFeeds;
import com.kktt.jesus.schedule.task.FeedBaseTask;
import com.kktt.jesus.schedule.task.FeedReportTask;
import com.kktt.jesus.service.AmazonMarketplaceService;
import com.kktt.jesus.service.RedisQueueService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public abstract class AmzFeedsBasicScheduler {
    protected static final Logger logger = LoggerFactory.getLogger(AmzFeedsBasicScheduler.class);
    @Resource
    protected TaskConsumerComponent taskConsumerComponent;
    @Resource
    protected AmazonMarketplaceService amazonMarketplaceService;
    @Resource
    protected MwsFeeds mwsFeeds;
    @Resource
    protected RedisQueueService redisQueueService;
    @Resource
    protected StringRedisTemplate stringRedisTemplate;
    @Value("${spring.profiles.active:dev}")
    protected String env;

    private static final String QUEUE_SMART_FEED_LIMIT_PREFIX = "GT_Queue_Feed_Limit_";

    protected abstract void createFeedRequest(String value, AmazonMarketplace amazonMarketplace) throws MWSRequestException, MarketplaceWebServiceException;

    protected abstract void saveReportResult(String reportResponse);

    protected abstract void afterGetDoneSubmissionId(List<String> submissionIdList);

    protected abstract Map<String, List<String>> getProcessingSubmissionList(byte type);

    protected void runCreateFeedRequest(String queueTasks) {
        taskConsumerComponent.consume(queueTasks, value -> {
            FeedBaseTask task = JSON.parseObject(value, FeedBaseTask.class);
            AmazonMarketplace amazonMarketplace = amazonMarketplaceService.forceFindActive(task.getId());
            forceValidateThrottlingLimited(amazonMarketplace);
            try {
                createFeedRequest(value, amazonMarketplace);
            } catch (MWSRequestThrottledException e) {
                createThrottlingLimited(task);
                throw e;
            }
        });
    }

    protected void doGetDoneSubmissionId(String queueName,byte type){
        //根据店铺分组 <key:sellerId#marketplaceId,value: List<SubmissionId>>
        Map<String, List<String>> tasksMap = getProcessingSubmissionList(type);
        tasksMap.forEach((k, v) -> {
            if (CollectionUtils.isNotEmpty(v)) {
                AmazonMarketplace amazonMarketplace = getShopEntity(k);
                List<String> submissionList = v.stream().distinct().collect(Collectors.toList());
                runGetDoneSubmissionId(amazonMarketplace,submissionList,queueName);
            }
        });
    }

    protected void runGetDoneSubmissionId(AmazonMarketplace amazonMarketplace, List<String> submissionIdList, String reportQueueName){
        List<String> submissionList = new ArrayList<>();
        int partSize = 30;
        if(submissionIdList.size() > partSize){
            int times = (submissionIdList.size() / partSize) +1;
            for (int i = 0; i < times; i++) {
                List<String> partList = submissionIdList.stream().skip(i * partSize).limit(partSize).collect(Collectors.toList());
                submissionList.addAll(getDoneSubmissionList(amazonMarketplace,partList));
            }
        }else{
            submissionList.addAll(getDoneSubmissionList(amazonMarketplace,submissionIdList));
        }
        if(CollectionUtils.isNotEmpty(submissionList)){
            //更新为已完成状态
            afterGetDoneSubmissionId(submissionIdList);
            //插入Redis
            List<String> taskStrList = new ArrayList<>();
            for (int i = 0; i < submissionIdList.size() ; i++) {
                FeedReportTask reportTask = new FeedReportTask();
                reportTask.setShopEntity(amazonMarketplace);
                reportTask.setSubmissionId(submissionIdList.get(i));
                taskStrList.add(JSON.toJSONString(reportTask));
            }
            redisQueueService.push(reportQueueName,taskStrList);
        }
    }

    protected void runGetDoneFeedReport(String reportQueueName){
        taskConsumerComponent.consume(reportQueueName, value -> {
            FeedReportTask task = JSON.parseObject(value, FeedReportTask.class);
            logger.info("feed报告 开始拉取，报告id:{}", task.getSubmissionId());
            forceValidateThrottlingLimited(task.getShopEntity());
            String result = mwsFeeds.getFeedSubmissionResult(task.getShopEntity(), task.getSubmissionId());

            logger.info("feed报告，解析并保存报告结果");
            saveReportResult(result);
            logger.info("feed报告 拉取结束，报告id:{}", task.getSubmissionId());
        });
    }

    private List<String> getDoneSubmissionList(AmazonMarketplace amazonMarketplace, List<String> partList) {
        if(CollectionUtils.isEmpty(partList)){
            return partList;
        }
        return getFeedSubmissionList(amazonMarketplace, partList);
    }

    private boolean isThrottlingLimited(long id) {
        return stringRedisTemplate.hasKey(QUEUE_SMART_FEED_LIMIT_PREFIX + id);
    }

    protected void createThrottlingLimited(FeedBaseTask task) {
        stringRedisTemplate.opsForValue().set(QUEUE_SMART_FEED_LIMIT_PREFIX + task.getId(), "1", 20, TimeUnit.SECONDS);
    }

    protected void forceValidateThrottlingLimited(AmazonMarketplace amazonMarketplace) throws MWSRequestThrottledException {
        if (!isThrottlingLimited(amazonMarketplace.getId()))
            return;
        throw new MWSRequestThrottledException("getLowestPrice Request Throttled", amazonMarketplace);
    }

    //获取已完成任务ID
    private List<String> getFeedSubmissionList(AmazonMarketplace amazonMarketplace, List<String> submissionIdList) {
        try {
            GetFeedSubmissionListResponse response = mwsFeeds.getFeedSubmissionList(amazonMarketplace,submissionIdList);
            return parseResponse(response);
        } catch (MarketplaceWebServiceException e) {
            logger.error("feed-状态 error:{}",e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<String> parseResponse(GetFeedSubmissionListResponse response) {
        List<String> submissionIdList = new ArrayList<>();
        if (!response.isSetGetFeedSubmissionListResult())
            return submissionIdList;

        GetFeedSubmissionListResult getFeedSubmissionListResult = response.getGetFeedSubmissionListResult();
        List<FeedSubmissionInfo> feedSubmissionInfoList = getFeedSubmissionListResult.getFeedSubmissionInfoList();
        for (FeedSubmissionInfo feedSubmissionInfo : feedSubmissionInfoList) {
            if (!feedSubmissionInfo.isSetFeedProcessingStatus())
                continue;

            if (MwsFeeds.DONE.equals(feedSubmissionInfo.getFeedProcessingStatus())) {
                logger.info("状态:{}--feedSubmissionId:{}", feedSubmissionInfo.getFeedProcessingStatus(), feedSubmissionInfo.getFeedSubmissionId());
                submissionIdList.add(feedSubmissionInfo.getFeedSubmissionId());
            }
        }
        return submissionIdList;
    }

    protected AmazonMarketplace getShopEntity(String k) {
        if (StringUtils.isNotEmpty(k)) {
            String sellerId = k.split("#")[0];
            String marketplaceId = k.split("#")[1];
            return amazonMarketplaceService.find(sellerId, marketplaceId);
        }
        return null;
    }

    protected ListingCreateRecordsEntity createListingRecord(AmazonMarketplace amazonMarketplace, String submissionId, Byte type){
        ListingCreateRecordsEntity createRecordsEntity = new ListingCreateRecordsEntity();
        createRecordsEntity.setSellerId(amazonMarketplace.getSellerId());
        createRecordsEntity.setMarketplaceId(amazonMarketplace.getMarketplaceId());
        createRecordsEntity.setStatus(ListingCreateRecordsEntity.STATUS.IN_PROGRESS);
        createRecordsEntity.setSubmissionId(submissionId);
        createRecordsEntity.setType(type);
        createRecordsEntity.setErrorInfo("");
        return createRecordsEntity;
    }
}