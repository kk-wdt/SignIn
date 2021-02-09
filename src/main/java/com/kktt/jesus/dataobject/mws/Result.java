package com.kktt.jesus.dataobject.mws;


/**
 * -<Result>
 *
 * <MessageID>0</MessageID>
 *
 * <ResultCode>Error</ResultCode>
 *
 * <ResultMessageCode>90215</ResultMessageCode>
 *
 * <ResultDescription>100% of the products in your file did not process successfully. We recommend using Check My File to help you identify and correct common listing errors before updating your inventory. To use Check My File, upload your file on the "Add Products via Upload" page in the "Check My File" section.</ResultDescription>
 *
 * </Result>
 */
public class Result {

    private String messageID;

    private String resultCode;

    private String resultMessageCode;

    private String resultDescription;

    private String SKU;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessageCode() {
        return resultMessageCode;
    }

    public void setResultMessageCode(String resultMessageCode) {
        this.resultMessageCode = resultMessageCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }
}
