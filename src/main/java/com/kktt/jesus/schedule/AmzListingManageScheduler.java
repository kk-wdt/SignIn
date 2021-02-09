package com.kktt.jesus.schedule;

import com.alibaba.fastjson.JSON;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.kktt.jesus.dao.source1.ListingFeedLogDao;
import com.kktt.jesus.dataobject.AmazonMarketplace;
import com.kktt.jesus.dataobject.ListingFeedLogEntity;
import com.kktt.jesus.dataobject.mws.ProcessingSummary;
import com.kktt.jesus.dataobject.mws.Result;
import com.kktt.jesus.exception.MWSRequestException;
import com.kktt.jesus.schedule.task.AliExpressListTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

@Component
public class AmzListingManageScheduler extends AmzListingManageBaseScheduler {
    @Resource
    private ListingFeedLogDao listingFeedLogDao;
    @Value("${spring.profiles.active:dev}")
    protected String env;

    public static final String DP_QUEUE_FEEDS_REQUEST_TASK = "DP_Queue_Feeds_Request_Task";//创建feed请求
    public static final String DP_QUEUE_FEEDS_REPORT_TASK = "DP_Queue_Feeds_Report_Task";//处理feed报告


    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 15 * 1000)
    public void runCreateFeedRequest() {
        runCreateFeedRequest(DP_QUEUE_FEEDS_REQUEST_TASK);
    }

    @Scheduled(fixedDelay = 45 * 1000, initialDelay = 20 * 1000)
    public void runGetDoneSubmissionId() {
       doGetDoneSubmissionId(DP_QUEUE_FEEDS_REPORT_TASK, (byte) 0);
    }

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 15 * 1000)
    public void runGetDoneFeedReport(){
        runGetDoneFeedReport(DP_QUEUE_FEEDS_REPORT_TASK);
    }

    @Override
    protected void createFeedRequest(AliExpressListTask task, AmazonMarketplace amazonMarketplace) throws MWSRequestException, MarketplaceWebServiceException {
        if(AliExpressListTask.TYPE.PRICE == task.getType()){
            logger.info("feed请求，创建改价请求");
            doPricing(task.getTasks(),amazonMarketplace,task.getUuid());
        }else if(AliExpressListTask.TYPE.INVENTORY == task.getType()){
            logger.info("feed请求，创建更改库存请求");
            doModifyInventory(task.getTasks(),amazonMarketplace,task.getUuid());
        }else if(AliExpressListTask.TYPE.DELETE == task.getType()){
            logger.info("feed请求，删除商品");
            doDeleteListing(task.getTasks(),amazonMarketplace,task.getUuid());
        }else {
            logger.error("feed create request error,not exist or not support feed type:{}",task.getType());
        }
    }

    @Override
    protected void saveReportResult(ProcessingSummary processingSummary) {
        BigInteger errNumber = processingSummary.getMessagesWithError();
        String submissionId = processingSummary.getSubmissionId();
        if(errNumber == null || errNumber.compareTo(BigInteger.ZERO) == 0){
            listingFeedLogDao.updateReportStatus(Collections.singletonList(submissionId), ListingFeedLogEntity.REPORT_STATUS.SUCCESS);
        }else{
            List<Result> errResultList = processingSummary.getResults();
            String errorInfo = JSON.toJSONString(errResultList);
            listingFeedLogDao.updateErrorInfo(submissionId,errorInfo);
        }
    }
}