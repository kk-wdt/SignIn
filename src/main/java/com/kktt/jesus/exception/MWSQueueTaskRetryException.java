package com.kktt.jesus.exception;


import com.kktt.jesus.dataobject.AmazonMarketplace;

public class MWSQueueTaskRetryException extends MWSRequestException {

    public MWSQueueTaskRetryException(String message, AmazonMarketplace amazonMarketplace) {
        super(message, amazonMarketplace);
    }

    public MWSQueueTaskRetryException(String message, String sellerId, String marketplaceId) {
        super(message, sellerId, marketplaceId);
    }
}