package com.kktt.jesus.exception;


import com.kktt.jesus.dataobject.AmazonMarketplace;

public class MWSRequestException extends Exception {

    private String sellerId;

    private String marketplaceId;

    private String message;

    public MWSRequestException(String message, AmazonMarketplace amazonMarketplace) {
        this.sellerId = amazonMarketplace.getSellerId();
        this.marketplaceId = amazonMarketplace.getMarketplaceId();
        this.message = message;
    }

    public MWSRequestException(String message, String sellerId, String marketplaceId) {
        this.sellerId = sellerId;
        this.marketplaceId = marketplaceId;
        this.message = message;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getMarketplaceId() {
        return marketplaceId;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
