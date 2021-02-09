package com.kktt.jesus.schedule.task;

public class AmazonMarketplaceTask extends BaseTask {

    private int id;

    public AmazonMarketplaceTask() {
    }

    public AmazonMarketplaceTask(Integer id) {
        this.id = id;
    }

    public AmazonMarketplaceTask(Integer id, long createTime) {
        this.id = id;
        setCreateTime(createTime);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}