package com.kktt.jesus.dataobject;

import java.sql.Timestamp;

public class OrderEntity {

    private int id;
    private String sellerId;
    private String marketplaceId;
    private String amazonOrderId;
    private Integer purchaseDate;
    private Integer lastUpdateDate;
    private String orderStatus;
    private Integer earliestShipDate;
    private Integer latestShipDate;
    private String fulfillmentChannel;
    private String salesChannel;
    private String countryCode;
    private String addrRegion;
    private String addrCity;
    private String postalCode;
    private String addrLine;
    private Float orderTotal;
    private String orderCurrency;
    private Integer itemsShipped;
    private Integer itemsUnshipped;
    private String buyerEmail;
    private String buyerName;
    private String orderType;
    private int isBusinessOrder;
    private Integer isPrime;
    private Integer isPremiumOrder;
    private Integer isReplacementOrder;
    private String replacedOrderId;
    private Integer isRefund;
    private Integer shipDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAmazonOrderId() {
        return amazonOrderId;
    }

    public void setAmazonOrderId(String amazonOrderId) {
        this.amazonOrderId = amazonOrderId;
    }

    public Integer getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Integer purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Integer lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getEarliestShipDate() {
        return earliestShipDate;
    }

    public void setEarliestShipDate(Integer earliestShipDate) {
        this.earliestShipDate = earliestShipDate;
    }

    public Integer getLatestShipDate() {
        return latestShipDate;
    }

    public void setLatestShipDate(Integer latestShipDate) {
        this.latestShipDate = latestShipDate;
    }

    public String getFulfillmentChannel() {
        return fulfillmentChannel;
    }

    public void setFulfillmentChannel(String fulfillmentChannel) {
        this.fulfillmentChannel = fulfillmentChannel;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAddrRegion() {
        return addrRegion;
    }

    public void setAddrRegion(String addrRegion) {
        this.addrRegion = addrRegion;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public void setAddrCity(String addrCity) {
        this.addrCity = addrCity;
    }

    public String getAddrLine() {
        return addrLine;
    }

    public void setAddrLine(String addrLine) {
        this.addrLine = addrLine;
    }

    public Float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderCurrency() {
        return orderCurrency;
    }

    public void setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
    }

    public Integer getItemsShipped() {
        return itemsShipped;
    }

    public void setItemsShipped(Integer itemsShipped) {
        this.itemsShipped = itemsShipped;
    }

    public Integer getItemsUnshipped() {
        return itemsUnshipped;
    }

    public void setItemsUnshipped(Integer itemsUnshipped) {
        this.itemsUnshipped = itemsUnshipped;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getIsBusinessOrder() {
        return isBusinessOrder;
    }

    public void setIsBusinessOrder(int isBusinessOrder) {
        this.isBusinessOrder = isBusinessOrder;
    }

    public Integer getIsPrime() {
        return isPrime;
    }

    public void setIsPrime(Integer isPrime) {
        this.isPrime = isPrime;
    }

    public Integer getIsPremiumOrder() {
        return isPremiumOrder;
    }

    public void setIsPremiumOrder(Integer isPremiumOrder) {
        this.isPremiumOrder = isPremiumOrder;
    }

    public Integer getIsReplacementOrder() {
        return isReplacementOrder;
    }

    public void setIsReplacementOrder(Integer isReplacementOrder) {
        this.isReplacementOrder = isReplacementOrder;
    }

    public String getReplacedOrderId() {
        return replacedOrderId;
    }

    public void setReplacedOrderId(String replacedOrderId) {
        this.replacedOrderId = replacedOrderId;
    }

    public Integer getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Integer isRefund) {
        this.isRefund = isRefund;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getShipDate() {
        return shipDate;
    }

    public void setShipDate(Integer shipDate) {
        this.shipDate = shipDate;
    }
}
