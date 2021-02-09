package com.kktt.jesus.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kktt.jesus.dao.source1.*;
import com.kktt.jesus.dataobject.*;
import com.kktt.jesus.dataobject.mws.ListingInfo;
import com.kktt.jesus.dataobject.mws.ProcessingSummary;
import com.kktt.jesus.dataobject.mws.VariationData;
import com.kktt.jesus.exception.MWSRequestException;
import com.kktt.jesus.mws.MwsFeeds;
import com.kktt.jesus.schedule.task.FeedReportTask;
import com.kktt.jesus.utils.FeedTsvUtil;
import com.kktt.jesus.utils.FeedsXmlUtil;
import com.kktt.jesus.utils.FileUtil;
import com.kktt.jesus.utils.VariationUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class AmzFlatFileListingBatchCreateScheduler extends AmzListingCreateBaseScheduler {
    public static final String SKU_PREFIX = "dps_";
    @Resource
    private ListingCreateRecordsDao listingCreateRecordsDao;
    @Value("${spring.profiles.active:dev}")
    protected String env;
    @Resource
    private AliexpressSkuPublishDao aliexpressSkuPublishDao;
    @Resource
    private AmazonCategoryTemplateDao amazonCategoryTemplateDao;
    @Resource
    private AmazonCategoryTemplateParamDao amazonCategoryTemplateParamDao;

    public static final String DP_QUEUE_FLAT_FILE_LISTING_CREATE_REPORT_TASK = "DP_Queue_Flat_File_Listing_Create_Report_Task";

    @Scheduled(fixedDelay = 300 * 1000, initialDelay = 15 * 1000)
    public void runCreateFeedRequest() {
        List<AliexpressSkuPublishEntity> taskList = aliexpressSkuPublishDao.query(AliexpressSkuPublishEntity.STATE.FINISH_IMAGE);
        if (CollectionUtils.isEmpty(taskList)) {
            logger.info("listing搬运 -- 暂无需要创建的任务");
            return;
        }
        dealWithTask(taskList);
    }

    /**
     * 处理出错的提交记录
     */
    @Scheduled(fixedDelay = 600 * 1000, initialDelay = 60 * 1000)
    public void dealWithErrorRequest() {
        //8541 8008 重新提交
        List<AliexpressSkuPublishEntity> errorTaskList = aliexpressSkuPublishDao.queryRetryTask();
        if (CollectionUtils.isEmpty(errorTaskList)) {
            logger.info("listing搬运 -- 无错误记录");
            return;
        }

        List<AliexpressSkuPublishEntity> legalProducts = new ArrayList<>();
        for (AliexpressSkuPublishEntity aliexpressSkuPublishEntity : errorTaskList) {
            if (!aliexpressSkuPublishEntity.getProperties().equals("{}") && StringUtils.isEmpty(aliexpressSkuPublishEntity.getSkuImageUrl())) {
            } else {
                legalProducts.add(aliexpressSkuPublishEntity);
            }
        }
        logger.info("listing搬运 -- 错误记录处理数量：{}", legalProducts.size());
        dealWithTask(legalProducts);
    }

    @Scheduled(fixedDelay = 45 * 1000, initialDelay = 20 * 1000)
    public void runGetDoneSubmissionId() {
        doGetDoneSubmissionId(DP_QUEUE_FLAT_FILE_LISTING_CREATE_REPORT_TASK, ListingCreateRecordsEntity.TYPE.FLAT_FILE_LISTING_CREATE);
    }

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 15 * 1000)
    public void runGetDoneFeedReport() {
        runGetDoneFeedReport(DP_QUEUE_FLAT_FILE_LISTING_CREATE_REPORT_TASK);
    }

    @Override
    protected void createFeedRequest(String value, AmazonMarketplace amazonMarketplace) throws MWSRequestException {
    }

    @Override
    protected void saveReportResult(ProcessingSummary processingSummary) {
    }

    private void dealWithTask(List<AliexpressSkuPublishEntity> taskList) {
        List<String> idList = taskList.stream().map(AliexpressSkuPublishEntity::getId).collect(Collectors.toList());
        int row = aliexpressSkuPublishDao.updateState(idList, AliexpressSkuPublishEntity.STATE.REQUEST);
        logger.info("listing搬运 -- 创建数量：{}", row);
        Map<String, List<AliexpressSkuPublishEntity>> shopNodeMap = taskList.stream().collect(Collectors.groupingBy(o -> o.getAmazonMarketplaceId() + "#" + o.getNodeId() + "#"));
        shopNodeMap.forEach((k, v) -> {
            String shopId = k.split("#")[0];
            String nodeId = k.split("#")[1];
            List<AliexpressSkuPublishEntity> shopNodeTaskList = shopNodeMap.get(k);
            requestCreateFlatFileListing(shopNodeTaskList, shopId, nodeId);
        });
    }

    protected void runGetDoneFeedReport(String reportQueueName) {
        taskConsumerComponent.consume(reportQueueName, value -> {
            FeedReportTask task = JSON.parseObject(value, FeedReportTask.class);
            logger.info("feed报告 开始拉取，报告id:{}", task.getSubmissionId());
            forceValidateThrottlingLimited(task.getShopEntity());
            String reportContent = mwsFeeds.getFeedSubmissionResult(task.getShopEntity(), task.getSubmissionId());
            StringReader stringReader = new StringReader(reportContent);
            CSVFormat csvFileFormat = CSVFormat.TDF.withQuote(null);
            CSVParser parser = new CSVParser(stringReader, csvFileFormat);
            List<CSVRecord> records = parser.getRecords();

            List<AliexpressSkuPublishEntity> aliexpressSkuPublishEntities = aliexpressSkuPublishDao.queryBySubmissionId(task.getSubmissionId());
            if (CollectionUtils.isNotEmpty(records) && records.size() > 3) {
                Map<String, List<String>> errorMap = parseErrorInfo(records);
                Set<String> errorSkuList = errorMap.keySet();
                for (AliexpressSkuPublishEntity item : aliexpressSkuPublishEntities) {
                    String sku = "{}".equals(item.getProperties()) ? getSku(item.getPrefix(), item.getProductId() + "",task.getShopEntity().getId()) : getSku(item.getPrefix(), item.getSkuId() + "",task.getShopEntity().getId());
                    if (!errorSkuList.contains(sku)) {
                        item.setState(AliexpressSkuPublishEntity.STATE.SUCCESS);
                    } else {
                        item.setState(AliexpressSkuPublishEntity.STATE.FAILURE);
                        List<String> errorInfo = errorMap.get(sku);
                        String errorString = errorInfo.stream().collect(Collectors.joining(","));
                        item.setError(errorString);
                    }
                }
                if (CollectionUtils.isNotEmpty(aliexpressSkuPublishEntities)) {
                    aliexpressSkuPublishDao.update(aliexpressSkuPublishEntities);
                }
                listingCreateRecordsDao.updateErrorInfo(task.getSubmissionId(), reportContent);
            } else {
                aliexpressSkuPublishDao.updateBySubmissionId(task.getSubmissionId());
                listingCreateRecordsDao.updateReportStatus(task.getSubmissionId(), ListingCreateRecordsEntity.REPORT_STATUS.SUCCESS, reportContent);
            }
        });
    }

    public Map<String, List<String>> parseErrorInfo(List<CSVRecord> records) {
        Map<String, List<String>> errorMap = new HashMap<>();
        for (int i = 4; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            if (record == null || record.size() < 4) {
                continue;
            }
            if (record.get(1) == null || record.get(4) == null) {
                continue;
            }

//            if("Warning".equalsIgnoreCase(record.get(3))){
//                continue;
//            }
            String sku = record.get(1);
            List<String> errorList = errorMap.get(sku);
            if (CollectionUtils.isEmpty(errorList)) {
                errorList = new ArrayList<>();
            }
            errorList.add(record.get(4));
            errorMap.put(sku, errorList);
        }
        return errorMap;
    }

    private void requestCreateFlatFileListing(List<AliexpressSkuPublishEntity> shopNodeTaskList, String shopId, String nodeId) {
        logger.info("listing搬运 -- shopId:{}, 创建数量：{}", shopId, shopNodeTaskList.size());

        //更新SKU前缀 只更新为空的数据
        List<Long> skuIdList = shopNodeTaskList.stream().map(AliexpressSkuPublishEntity::getSkuId).collect(Collectors.toList());

        String xmlPath = FileUtil.getFilePath();
        try {
            AmazonMarketplace amazonMarketplace = amazonMarketplaceService.forceFindActive(Integer.parseInt(shopId));
            aliexpressSkuPublishDao.updatePrefix(shopId, skuIdList, amazonMarketplace.getPrefix());
            AmazonsCategoryTemplateEntity template = amazonCategoryTemplateDao.find(nodeId);
            AmazonsCategoryTemplateParamEntity variationTheme = amazonCategoryTemplateParamDao.findVariationTheme(amazonMarketplace.getMarketplaceId(), nodeId);
            if(variationTheme == null){
                logger.info("不支持变体的类目,nodeId:{};shopId:{}",nodeId,shopId);
                return;
            }
            List<ListingInfo> listingInfos = convertToListingInfo(shopNodeTaskList,variationTheme.getValues());
            String tsv = FeedTsvUtil.batchCreateRequestContent(listingInfos, template);
//            logger.info("创建的TSV:{}/n",tsv);
            String submissionId = mwsFeeds.submitFeed(amazonMarketplace, FeedsXmlUtil.conventToStream(tsv, xmlPath,amazonMarketplace.getMarketplaceId()), MwsFeeds.FLAT_FILE_LISTINGS_FEED);
            if (StringUtils.isNotEmpty(submissionId)) {
                ListingCreateRecordsEntity createRecord = createListingRecord(amazonMarketplace, submissionId, ListingCreateRecordsEntity.TYPE.FLAT_FILE_LISTING_CREATE);
                listingCreateRecordsDao.insertOne(createRecord);
                //标识同一批提交的创建请求
                List<Long> productIdList = shopNodeTaskList.stream().map(AliexpressSkuPublishEntity::getProductId).distinct().collect(Collectors.toList());
                aliexpressSkuPublishDao.updateSubmission(shopId, submissionId, productIdList);
            } else {
                logger.error("submitFeed创建出错,submitFeed：{}", submissionId);
                rollbackState(shopNodeTaskList);
            }
        } catch (Exception e) {
            logger.error("批量创建listing error:{},shopId:{},nodeId:{}", e.getMessage(), shopId, nodeId);
            rollbackState(shopNodeTaskList);
            e.printStackTrace();
        } finally {
            FileUtil.deleteFile(xmlPath);
        }
    }

    private void rollbackState(List<AliexpressSkuPublishEntity> errorList) {
        //提交创建失败回滚状态
        for (AliexpressSkuPublishEntity entity : errorList) {
            entity.setState(AliexpressSkuPublishEntity.STATE.FINISH_IMAGE);
        }
        aliexpressSkuPublishDao.update(errorList);
    }

    public List<ListingInfo> convertToListingInfo(List<AliexpressSkuPublishEntity> shopNodeTaskList, String variationThemeStr) {
        List<ListingInfo> listingInfos = new ArrayList<>();
        Map<Long, List<AliexpressSkuPublishEntity>> productMap = shopNodeTaskList.stream().collect(Collectors.groupingBy(AliexpressSkuPublishEntity::getProductId));
        productMap.forEach((productId, values) -> {
            if (CollectionUtils.isEmpty(values)) {
                return;
            }
            AliexpressSkuPublishEntity task = values.get(0);
            ListingInfo listingInfo = new ListingInfo();
            //设置基础信息
            setBasicData(listingInfo, task);
            if (listingInfo.getIsVariation() == 1) {
                //设置变体信息
                setVariation(listingInfo, values);
                String properties = values.get(0).getProperties();
                JSONObject propertyJson = JSON.parseObject(properties);
                JSONArray variationThemeArr = JSON.parseArray(variationThemeStr);
                listingInfo.setVariationTheme(VariationUtil.matchVariationTheme(propertyJson,variationThemeArr));
            }
            listingInfos.add(listingInfo);
        });
        return listingInfos;
    }

    private String getSku(String prefix, String flagId,int amazonMarketplaceId) {
        if (StringUtils.isEmpty(prefix)) {
            AmazonMarketplace amazonMarketplace = amazonMarketplaceService.findById(amazonMarketplaceId);
            return SKU_PREFIX +amazonMarketplace.getPrefix()+ flagId;
        } else {
            return prefix + flagId;
        }
    }

    private void setBasicData(ListingInfo listingInfo, AliexpressSkuPublishEntity task) {
        listingInfo.setSku(getSku(task.getPrefix(), task.getProductId() + "",task.getAmazonMarketplaceId()));
        listingInfo.setIsVariation(StringUtils.isEmpty(task.getProperties()) || "{}".equals(task.getProperties()) ? (byte) 0 : (byte) 1);
        listingInfo.setCondition("New");
        listingInfo.setRecommendedBrowseNode(task.getNodeId());
        String mainImages = task.getImageUrls();
        List<String> imageList = JSON.parseArray(mainImages, String.class);
        if (CollectionUtils.isNotEmpty(imageList)) {
            listingInfo.setMainImage(imageList.get(0));
            imageList.remove(0);
            listingInfo.setExtraImages(String.join(",", imageList));
        }

        JSONObject valueObj = JSON.parseObject(task.getNodeValue());
        String itemType = valueObj.getString("item_type");
        listingInfo.setItemType(itemType);
        listingInfo.setBrand(valueObj.getString("brand_name"));
        listingInfo.setManufacturer(valueObj.getString("manufacturer"));
        listingInfo.setProductType(valueObj.getString("feed_product_type"));

        listingInfo.setDescription(valueObj.getString("description"));
        String bulletPoints = valueObj.getString("bullet_points");
        listingInfo.setBulletPoints(bulletPoints);
        listingInfo.setTitle(task.getTitle());
        if (listingInfo.getIsVariation() == 0) {
            listingInfo.setStandardProductType("UPC");
            listingInfo.setStandardProductValue(getUpc(task));
            listingInfo.setStandardPrice(task.getPrice());
            listingInfo.setQuantity(task.getInventory());
        }
        JSONObject jobj = JSON.parseObject(task.getNodeValue());
        if(task.getUpdateDelete() == 1){
            jobj.put("update_delete","PartialUpdate");
        }
        listingInfo.setProductData(jobj);
    }

    private void setVariation(ListingInfo listingInfo, List<AliexpressSkuPublishEntity> values) {
        List<VariationData> variationData = new ArrayList<>();
        for (AliexpressSkuPublishEntity aliexpress : values) {
            VariationData variation = new VariationData();
            variation.setQuantity(aliexpress.getInventory());
            variation.setPrice(aliexpress.getPrice());
            variation.setCondition("New");
            variation.setStandardProductType("UPC");
            variation.setStandardProductValue(getUpc(aliexpress));
            variation.setSku(getSku(aliexpress.getPrefix(), aliexpress.getSkuId() + "",aliexpress.getAmazonMarketplaceId()));

            JSONObject paramJson = new JSONObject();
            String properties = aliexpress.getProperties();
            JSONObject propertyJson = JSON.parseObject(properties);
            for (String key : propertyJson.keySet()) {
                String value = propertyJson.getString(key);
                if (key.equalsIgnoreCase("Color")) {
                    paramJson.put("color_name", value);
                    paramJson.put("color_map", value);
                }
                if (key.equalsIgnoreCase("Size")) {
                    paramJson.put("size_name", value);
                    paramJson.put("size_map", value);
                }
            }
            paramJson.put("image", aliexpress.getSkuImageUrl());
            if(aliexpress.getUpdateDelete() == 1){
                paramJson.put("update_delete","PartialUpdate");
            }
            variation.setParamData(paramJson);
            variationData.add(variation);
        }
        listingInfo.setVariationData(variationData);
    }

    @Resource
    private UpcDao upcDao;

    private String getUpc(AliexpressSkuPublishEntity task) {
        if(StringUtils.isNotEmpty(task.getUpc()) && task.getState().equals(AliexpressSkuPublishEntity.STATE.SUCCESS)){
            return task.getUpc();
        }

        List<UpcEntity> upcList = upcDao.fetch(1);
        if(CollectionUtils.isEmpty(upcList)){
            throw new IllegalArgumentException("UPC 码不足");
        }
        String upc = upcList.get(0).getUpc();
        aliexpressSkuPublishDao.updateUpc(task.getAmazonMarketplaceId(),task.getSkuId(),upc);
        upcDao.used(upc);
        return upc;
    }

    public void syncState() {
        List<AliexpressSkuPublishEntity> dongTask = aliexpressSkuPublishDao.query(AliexpressSkuPublishEntity.STATE.REQUEST);
        Map<String, List<AliexpressSkuPublishEntity>> submissionMap = dongTask.stream().collect(Collectors.groupingBy(AliexpressSkuPublishEntity::getSubmissionId));
        submissionMap.forEach((submissionId, aliexpressSkuPublishEntities) -> {
            ListingCreateRecordsEntity record = listingCreateRecordsDao.find(submissionId);
            if (record == null) {
                return;
            }

            String reportContent = record.getErrorInfo();
            StringReader stringReader = new StringReader(reportContent);
            CSVFormat csvFileFormat = CSVFormat.TDF.withQuote(null);
            try {
                CSVParser parser = new CSVParser(stringReader, csvFileFormat);
                List<CSVRecord> records = parser.getRecords();
                if (CollectionUtils.isNotEmpty(records) && records.size() > 3) {
                    Map<String, List<String>> errorMap = parseErrorInfo(records);
                    Set<String> errorSkuList = errorMap.keySet();
                    for (AliexpressSkuPublishEntity item : aliexpressSkuPublishEntities) {
                        String sku = "{}".equals(item.getProperties()) ? getSku(item.getPrefix(), item.getProductId() + "",item.getAmazonMarketplaceId()) : getSku(item.getPrefix(), item.getSkuId() + "",item.getAmazonMarketplaceId());
                        if (!errorSkuList.contains(sku)) {
                            item.setState(AliexpressSkuPublishEntity.STATE.SUCCESS);
                        } else {
                            item.setState(AliexpressSkuPublishEntity.STATE.FAILURE);
                            List<String> errorInfo = errorMap.get(sku);
                            String errorString = String.join(",", errorInfo);
                            item.setError(errorString);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(aliexpressSkuPublishEntities)) {
                        aliexpressSkuPublishDao.update(aliexpressSkuPublishEntities);
                    }
                } else {
                    aliexpressSkuPublishDao.updateBySubmissionId(submissionId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}