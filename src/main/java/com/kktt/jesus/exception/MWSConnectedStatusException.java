package com.kktt.jesus.exception;


import com.kktt.jesus.dataobject.AmazonMarketplace;

public class MWSConnectedStatusException extends MWSRequestException {

    public MWSConnectedStatusException(String message, AmazonMarketplace amazonMarketplace) {
        super(message, amazonMarketplace);
    }

    public MWSConnectedStatusException(String message, String sellerId, String marketplaceId) {
        super(message, sellerId, marketplaceId);
    }
}