package com.kktt.jesus.exception;

public class MWSInvalidParameterValueException extends MWSRequestException {

    public MWSInvalidParameterValueException(String message, String sellerId, String marketplaceId) {
        super(message, sellerId, marketplaceId);
    }
}
