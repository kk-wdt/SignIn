package com.kktt.jesus.dataobject;

public class ListingCreateRecordsEntity {

    public interface TYPE{
        byte PRODUCT_CREATE = 10;//创建listing
        byte PRICE_UPDATE = 20;//更新价格
        byte INVENTORY_UPDATE = 30;//更新库存
        byte IMAGE_UPDATE = 40;//更新图片
        byte VARIATION = 50;//变体关系
        byte FLAT_FILE_LISTING_CREATE = 60;
    }
    public interface REPORT_STATUS{
        Integer SUCCESS = 10;//成功
        Integer FAILED = 20;//失败
    }
    public interface STATUS{
        Integer IN_PROGRESS = 10; //执行中
        Integer DONE = 20;//执行完成
    }

    private Integer id;

    private String sellerId;

    private String marketplaceId;

    private String uuid;

    private String submissionId;

    private Integer status;

    private Integer reportStatus;

    private Byte  type;

    private String errorInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
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

    public Integer getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Integer reportStatus) {
        this.reportStatus = reportStatus;
    }
}
