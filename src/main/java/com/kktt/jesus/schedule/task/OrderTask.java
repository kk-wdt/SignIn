package com.kktt.jesus.schedule.task;

public class OrderTask extends BaseTask {

    private Long shopId;
    private Long startEpochSecond;
    private Long endEpochSecond;
    private String nextToken;

    public OrderTask() {
    }

    public OrderTask(Long shopId, Long startEpochSecond, Long endEpochSecond) {
        this.shopId = shopId;
        this.startEpochSecond = startEpochSecond;
        this.endEpochSecond = endEpochSecond;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStartEpochSecond() {
        return startEpochSecond;
    }

    public void setStartEpochSecond(Long startEpochSecond) {
        this.startEpochSecond = startEpochSecond;
    }

    public Long getEndEpochSecond() {
        return endEpochSecond;
    }

    public void setEndEpochSecond(Long endEpochSecond) {
        this.endEpochSecond = endEpochSecond;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
