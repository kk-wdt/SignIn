package com.kktt.jesus.schedule.task;

import lombok.Data;

import java.util.List;

@Data
public class ProductMonitorTask {

    private String site;

    private Integer amazonMarketplaceId;

    private List<Long> productIdList;


}
