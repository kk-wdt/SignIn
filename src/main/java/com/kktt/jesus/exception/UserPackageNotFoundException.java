package com.kktt.jesus.exception;
/**
 * 用户无当前套餐
 * @author hongqinggong
 * @since 2017年12月27日
 */
@SuppressWarnings("serial")
public class UserPackageNotFoundException extends Exception {

    public UserPackageNotFoundException() {
        super("Your package has expired!");
    }

    public UserPackageNotFoundException(String message) {
        super(message);
    }

    public UserPackageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
