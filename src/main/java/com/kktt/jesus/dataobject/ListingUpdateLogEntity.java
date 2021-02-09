package com.kktt.jesus.dataobject;

import java.util.Date;

public class ListingUpdateLogEntity {
    public interface TYPE{
        Integer PRICE = 10;
        Integer INVENTORY = 20;
        Integer MFN = 30;
        Integer AFN = 40;
    }

    public interface STATUS{
        Integer IN_PROGRESS = 10; //执行中
        Integer DONE = 20;//执行完成
    }

    private Integer id;

    private String submissionId;

    private String sellerId;

    private String marketplaceId;

    private Integer status;

    private Integer  type;

    private Date createdAt;

    public ListingUpdateLogEntity() {
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getMarketplaceId() {
        return marketplaceId;
    }

    public void setMarketplaceId(String marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
