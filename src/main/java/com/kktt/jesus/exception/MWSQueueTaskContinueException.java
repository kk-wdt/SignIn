package com.kktt.jesus.exception;


import com.kktt.jesus.dataobject.AmazonMarketplace;

public class MWSQueueTaskContinueException extends MWSRequestException {

    public MWSQueueTaskContinueException(String message, AmazonMarketplace amazonMarketplace) {
        super(message, amazonMarketplace);
    }

    public MWSQueueTaskContinueException(String message, String sellerId, String marketplaceId) {
        super(message, sellerId, marketplaceId);
    }
}