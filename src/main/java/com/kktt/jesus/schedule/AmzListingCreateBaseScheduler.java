package com.kktt.jesus.schedule;

import com.kktt.jesus.dao.source1.ListingCreateRecordsDao;
import com.kktt.jesus.dataobject.ListingCreateRecordsEntity;
import com.kktt.jesus.dataobject.mws.ProcessingSummary;
import com.kktt.jesus.utils.FeedsXmlUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public abstract class AmzListingCreateBaseScheduler extends AmzFeedsBasicScheduler{
    protected static final Logger logger = LoggerFactory.getLogger(AmzListingCreateBaseScheduler.class);
    @Resource
    private ListingCreateRecordsDao listingCreateRecordsDao;

    protected abstract void saveReportResult(ProcessingSummary processingSummary);

    @Override
    protected Map<String, List<String>> getProcessingSubmissionList(byte type) {
        List<ListingCreateRecordsEntity> processingTasks = listingCreateRecordsDao.queryByStatus(ListingCreateRecordsEntity.STATUS.IN_PROGRESS,type);
        if(CollectionUtils.isEmpty(processingTasks)){
            logger.info("Feed:Pricing - 无需要改价的记录 跳过");
            return new HashMap<>();
        }
        logger.info("Feed:doGetDoneSubmissionId - {}条需要修改的记录",processingTasks.size());
        //根据店铺分组 <key:sellerId#marketplaceId,value: List<ListingCreateRecordsEntity>>
        Map<String, List<ListingCreateRecordsEntity>> tasksMap = processingTasks.stream().collect(Collectors.groupingBy(t -> t.getSellerId() + "#" + t.getMarketplaceId()));
        Map<String,List<String>> taskSubmissionMap = new HashMap<>();
        tasksMap.forEach((k,v)->{
            taskSubmissionMap.put(k,v.stream().map(ListingCreateRecordsEntity::getSubmissionId).distinct().collect(Collectors.toList()));
        });
        return taskSubmissionMap;
    }

    @Override
    protected void afterGetDoneSubmissionId(List<String> submissionIdList) {
        //更新为已完成状态
        listingCreateRecordsDao.updateStatus(submissionIdList, ListingCreateRecordsEntity.STATUS.DONE);
    }

    @Override
    protected void saveReportResult(String reportResponse) {
        ProcessingSummary processingSummary = FeedsXmlUtil.parseResponseXml(new ByteArrayInputStream(reportResponse.getBytes()));
        saveReportResult(processingSummary);
    }

}