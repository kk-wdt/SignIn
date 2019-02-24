package com.kktt.jesus.errror;

public enum  EmBusinessError implements CommonError{
    UNKNOWN_ERROR(99999,"未知错误"),
    //定义通用错误类型
    PARAMETER_VALIDATION_ERROR(10001,"参数错误"),

    USER_NOT_EXIST(20001,"用户不存在")
    ;
    private int errCode;
    private String errMsg;

    private EmBusinessError(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    @Override
    public int getErrCode() {
        return errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
