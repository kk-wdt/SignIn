package com.kktt.jesus.schedule.task;

import java.time.Instant;

public class BaseTask {

    private long createTime = Instant.now().getEpochSecond();

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
