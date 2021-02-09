package com.kktt.jesus.exception;


import com.kktt.jesus.dataobject.AmazonMarketplace;

public class MWSRequestThrottledException extends MWSRequestException {

    public MWSRequestThrottledException(String message, AmazonMarketplace amazonMarketplace) {
        super(message, amazonMarketplace);
    }

    public MWSRequestThrottledException(String message, String sellerId, String marketplaceId) {
        super(message, sellerId, marketplaceId);
    }
}