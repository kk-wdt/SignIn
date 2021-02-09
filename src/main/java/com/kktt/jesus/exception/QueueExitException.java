package com.kktt.jesus.exception;

public class QueueExitException extends RuntimeException {

    public QueueExitException() {
    }

    public QueueExitException(String message) {
        super(message);
    }
}
