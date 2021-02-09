package com.kktt.jesus.exception;

public class MWSAuthTokenInvalidException extends MWSRequestException {

    public MWSAuthTokenInvalidException(String message, String sellerId, String marketplaceId) {
        super(message, sellerId, marketplaceId);
    }
}