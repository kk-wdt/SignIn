package com.kktt.jesus.schedule.task;


import com.kktt.jesus.dataobject.AmazonMarketplace;

import java.time.Instant;

public class FeedReportTask extends BaseTask {

    private AmazonMarketplace amazonMarketplace;

    private String submissionId;

    private Long dayEpochSecond = Instant.now().getEpochSecond();

    public FeedReportTask() {
    }

    public AmazonMarketplace getShopEntity() {
        return amazonMarketplace;
    }

    public void setShopEntity(AmazonMarketplace amazonMarketplace) {
        this.amazonMarketplace = amazonMarketplace;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public Long getDayEpochSecond() {
        return dayEpochSecond;
    }

    public void setDayEpochSecond(Long dayEpochSecond) {
        this.dayEpochSecond = dayEpochSecond;
    }
}
