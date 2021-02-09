package com.kktt.jesus.schedule.task;

import java.util.List;

public class GeneratedFeedTask extends FeedBaseTask {
    public interface TYPE{
        byte PRICE = 10;
        byte INVENTORY = 20;
    }

    private List<String> skuList;

    private byte type;

    private String from;

    private String to;
    public GeneratedFeedTask() {
    }

    public String getFrom() {
        return from;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<String> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<String> skuList) {
        this.skuList = skuList;
    }
}