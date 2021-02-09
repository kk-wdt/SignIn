package com.kktt.jesus.schedule;

import com.alibaba.fastjson.JSON;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonservices.mws.client.MwsException;
import com.kktt.jesus.dao.source1.ListingFeedLogDao;
import com.kktt.jesus.dataobject.AliExpressItem;
import com.kktt.jesus.dataobject.AmazonMarketplace;
import com.kktt.jesus.dataobject.ListingFeedLogEntity;
import com.kktt.jesus.dataobject.mws.Inventory;
import com.kktt.jesus.dataobject.mws.Price;
import com.kktt.jesus.dataobject.mws.ProcessingSummary;
import com.kktt.jesus.dataobject.mws.StandardPrice;
import com.kktt.jesus.exception.MWSRequestException;
import com.kktt.jesus.mws.MwsFeeds;
import com.kktt.jesus.schedule.task.AliExpressListTask;
import com.kktt.jesus.utils.FeedsXmlUtil;
import com.kktt.jesus.utils.FileUtil;
import com.kktt.jesus.utils.MWSExceptionIdentifyUtil;
import com.kktt.jesus.utils.MwsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public abstract class AmzListingManageBaseScheduler extends AmzFeedsBasicScheduler{
    @Resource
    private ListingFeedLogDao listingFeedLogDao;

    protected abstract void createFeedRequest(AliExpressListTask task, AmazonMarketplace amazonMarketplace) throws MWSRequestException, MarketplaceWebServiceException;

    protected abstract void saveReportResult(ProcessingSummary processingSummary);

    @Override
    protected Map<String, List<String>> getProcessingSubmissionList(byte type) {
        List<ListingFeedLogEntity> processingTasks = listingFeedLogDao.queryByStatus(ListingFeedLogEntity.STATUS.IN_PROGRESS);
        if(CollectionUtils.isEmpty(processingTasks)){
            logger.info("Feed:Pricing - 无需要改价的记录 跳过");
            return new HashMap<>();
        }
        logger.info("Feed:doGetDoneSubmissionId - {}条需要修改的记录",processingTasks.size());
        //根据店铺分组 <key:sellerId#marketplaceId,value: List<ListingCreateRecordsEntity>>
        Map<String, List<ListingFeedLogEntity>> tasksMap = processingTasks.stream().collect(Collectors.groupingBy(t -> t.getSellerId() + "#" + t.getMarketplaceId()));
        Map<String,List<String>> taskSubmissionMap = new HashMap<>();
        tasksMap.forEach((k,v)->{
            taskSubmissionMap.put(k,v.stream().map(ListingFeedLogEntity::getSubmissionId).distinct().collect(Collectors.toList()));
        });
        return taskSubmissionMap;
    }

    @Override
    protected void createFeedRequest(String value, AmazonMarketplace amazonMarketplace) throws MWSRequestException, MarketplaceWebServiceException {
        AliExpressListTask aliExpressListTask = JSON.parseObject(value,AliExpressListTask.class);
        createFeedRequest(aliExpressListTask ,amazonMarketplace);
    }

    @Override
    protected void afterGetDoneSubmissionId(List<String> submissionIdList) {
        //更新为已完成状态
        listingFeedLogDao.updateStatus(submissionIdList, ListingFeedLogEntity.STATUS.DONE);
    }

    @Override
    protected void saveReportResult(String reportResponse) {
        ProcessingSummary processingSummary = FeedsXmlUtil.parseResponseXml(new ByteArrayInputStream(reportResponse.getBytes()));
        saveReportResult(processingSummary);
    }

    //批量改价
    protected void doPricing(List<AliExpressItem> taskList, AmazonMarketplace amazonMarketplace, String uuid) throws MarketplaceWebServiceException {
        String xmlPath = FileUtil.getFilePath();
        String currency = MwsUtil.getAmazonCurrencyCode(amazonMarketplace.getMarketplaceId());
        try{
            List<Price> prices = new ArrayList<>();
            for (AliExpressItem task:taskList) {
                BigDecimal to = new BigDecimal(task.getPrice()+"");
                prices.add(new Price(task.getSku()+"",new StandardPrice(to,currency)));
            }
            String xml = FeedsXmlUtil.createPriceFeedXml(amazonMarketplace.getSellerId(),prices);
            String submissionId = mwsFeeds.submitFeed(amazonMarketplace, FeedsXmlUtil.conventToStream(xml,xmlPath), MwsFeeds.PRICING_FEED_TYPE);
            if(StringUtils.isNotEmpty(submissionId)){
                saveFeedRecord(amazonMarketplace,submissionId,ListingFeedLogEntity.TYPE.UPDATE_PRICE,uuid);
            }
        } finally {
            FileUtil.deleteFile(xmlPath);
        }
    }

    public void doModifyInventory(List<AliExpressItem> taskList, AmazonMarketplace amazonMarketplace, String uuid) throws MWSRequestException {
        String xmlPath = FileUtil.getFilePath();
        try{
            List<Inventory> inventories = new ArrayList<>();
            for (AliExpressItem sku:taskList) {
                inventories.add(new Inventory(sku.getSku()+"",sku.getInventory()));
            }
            String xml = FeedsXmlUtil.createInventoryQuantityFeedXml(amazonMarketplace.getSellerId(),inventories);
            String submissionId = mwsFeeds.submitFeed(amazonMarketplace, FeedsXmlUtil.conventToStream(xml,xmlPath), MwsFeeds.INVENTORY_FEED_TYPE);
            if(StringUtils.isNotEmpty(submissionId)){
                saveFeedRecord(amazonMarketplace,submissionId,ListingFeedLogEntity.TYPE.UPDATE_INVENTORY,uuid);
            }
        } catch (MwsException e) {
            MWSExceptionIdentifyUtil.throwIt(e, amazonMarketplace);
        } catch (MarketplaceWebServiceException e) {
            MWSExceptionIdentifyUtil.throwIt(e, amazonMarketplace);
        } finally {
            FileUtil.deleteFile(xmlPath);
        }
    }

    public void doDeleteListing(List<AliExpressItem> taskList, AmazonMarketplace amazonMarketplace, String uuid) throws MWSRequestException {
        String xmlPath = FileUtil.getFilePath();
        try{
            String xml = FeedsXmlUtil.createDeleteListingXml(amazonMarketplace.getSellerId(),taskList).asXML();
            String submissionId = mwsFeeds.submitFeed(amazonMarketplace, FeedsXmlUtil.conventToStream(xml,xmlPath), MwsFeeds.INVENTORY_FEED_TYPE);
            if(StringUtils.isNotEmpty(submissionId)){
                saveFeedRecord(amazonMarketplace,submissionId,ListingFeedLogEntity.TYPE.DELETE_LISTING,uuid);
            }
        } catch (MwsException e) {
            MWSExceptionIdentifyUtil.throwIt(e, amazonMarketplace);
        } catch (MarketplaceWebServiceException e) {
            MWSExceptionIdentifyUtil.throwIt(e, amazonMarketplace);
        } finally {
            FileUtil.deleteFile(xmlPath);
        }
    }

    protected void saveFeedRecord(AmazonMarketplace amazonMarketplace, String submissionId, byte type,String uuid) {
        //保存
        ListingFeedLogEntity pricingLogEntity = new ListingFeedLogEntity();
        pricingLogEntity.setSellerId(amazonMarketplace.getSellerId());
        pricingLogEntity.setMarketplaceId(amazonMarketplace.getMarketplaceId());
        pricingLogEntity.setStatus(ListingFeedLogEntity.STATUS.IN_PROGRESS);
        pricingLogEntity.setSubmissionId(submissionId);
        pricingLogEntity.setUuid(uuid);
        pricingLogEntity.setType(type);
        listingFeedLogDao.insertOne(pricingLogEntity);
    }

}