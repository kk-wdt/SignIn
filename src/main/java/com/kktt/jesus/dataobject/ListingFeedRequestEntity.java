package com.kktt.jesus.dataobject;

public class ListingFeedRequestEntity {

    public interface STATUS{
        Byte UN_DO = 10; //未执行
        Byte DOING = 20;
        Byte DONE = 30;//执行完成
    }
    public interface TYPE{
        byte PRICE = 10;
        byte INVENTORY = 20;
    }

    private Integer id;

    private String sku;

    private String sellerId;

    private String marketplaceId;

    private Byte status;

    private Byte type;

    private String to;

    private String uuid;

    private Long createTime;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
}
