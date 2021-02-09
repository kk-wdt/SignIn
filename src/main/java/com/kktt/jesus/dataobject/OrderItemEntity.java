package com.kktt.jesus.dataobject;

import java.sql.Timestamp;

public class OrderItemEntity {

    private int id;
    private String sellerId;
    private String marketplaceId;
    private String amazonOrderId;
    private String orderItemId;
    private String asin;
    private String sku;
    private String title;

    private Integer quantityOrdered;
    private Integer quantityShipped;
    private Integer pointsGranted;

    private Float itemPrice;
    private Float shippingPrice;
    private Float giftWrapPrice;
    private Float itemTax;
    private Float shippingTax;
    private Float giftWrapTax;
    private Float shippingDiscount;
    private Float promotionDiscount;
    private Float codFee;
    private Float codFeeDiscount;

    private Integer purchaseDate;
    private Timestamp createAt;
    private Timestamp updateAt;

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

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(Integer quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public Integer getQuantityShipped() {
        return quantityShipped;
    }

    public void setQuantityShipped(Integer quantityShipped) {
        this.quantityShipped = quantityShipped;
    }

    public Integer getPointsGranted() {
        return pointsGranted;
    }

    public void setPointsGranted(Integer pointsGranted) {
        this.pointsGranted = pointsGranted;
    }

    public Float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Float getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(Float shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public Float getGiftWrapPrice() {
        return giftWrapPrice;
    }

    public void setGiftWrapPrice(Float giftWrapPrice) {
        this.giftWrapPrice = giftWrapPrice;
    }

    public Float getItemTax() {
        return itemTax;
    }

    public void setItemTax(Float itemTax) {
        this.itemTax = itemTax;
    }

    public Float getShippingTax() {
        return shippingTax;
    }

    public void setShippingTax(Float shippingTax) {
        this.shippingTax = shippingTax;
    }

    public Float getGiftWrapTax() {
        return giftWrapTax;
    }

    public void setGiftWrapTax(Float giftWrapTax) {
        this.giftWrapTax = giftWrapTax;
    }

    public Float getShippingDiscount() {
        return shippingDiscount;
    }

    public void setShippingDiscount(Float shippingDiscount) {
        this.shippingDiscount = shippingDiscount;
    }

    public Float getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(Float promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public Float getCodFee() {
        return codFee;
    }

    public void setCodFee(Float codFee) {
        this.codFee = codFee;
    }

    public Float getCodFeeDiscount() {
        return codFeeDiscount;
    }

    public void setCodFeeDiscount(Float codFeeDiscount) {
        this.codFeeDiscount = codFeeDiscount;
    }

    public Integer getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Integer purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }
}
