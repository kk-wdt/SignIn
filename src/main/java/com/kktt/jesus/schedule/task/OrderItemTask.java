package com.kktt.jesus.schedule.task;

public class OrderItemTask extends BaseTask {

    private String orderId;
    private Long shopId;
    private Integer purchaseDate;
    private String nextToken;

    public OrderItemTask() {
    }

    public OrderItemTask(long shopId, String orderId, int purchaseDate) {
        this.shopId = shopId;
        this.orderId = orderId;
        this.purchaseDate = purchaseDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Integer purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
