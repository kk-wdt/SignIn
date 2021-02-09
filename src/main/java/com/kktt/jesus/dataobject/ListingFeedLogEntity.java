package com.kktt.jesus.dataobject;

public class ListingFeedLogEntity {

    public interface STATUS{
        Integer IN_PROGRESS = 10; //执行中
        Integer DONE = 20;//执行完成
    }

    public interface REPORT_STATUS{
        Integer SUCCESS = 10;//成功
        Integer FAILED = 20;//失败
        Integer IGNORE = 30;
    }

    public interface TYPE{
        Byte UPDATE_PRICE = 1;//修改价格
        Byte UPDATE_INVENTORY = 2;//修改库存
        Byte DELETE_LISTING = 3;//删除listing
    }


    private Integer id;

    private String submissionId;

    private String sellerId;

    private String marketplaceId;

    private Integer status;

    private Integer reportStatus;

    private String uuid;

    private Byte type;


    public ListingFeedLogEntity() {
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
    public Integer getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Integer reportStatus) {
        this.reportStatus = reportStatus;
    }

}
