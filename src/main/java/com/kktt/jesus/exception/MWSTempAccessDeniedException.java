package com.kktt.jesus.exception;

public class MWSTempAccessDeniedException extends MWSRequestException {

    public MWSTempAccessDeniedException(String message, String sellerId, String marketplaceId) {
        super(message, sellerId, marketplaceId);
    }
}