package com.kktt.jesus.dataobject;

import lombok.Data;

@Data
public class ListingFeedLogItemEntity {
    public interface STATUS{
        Integer DOING = 0;
        Integer DONE = 1;
    }

    private Integer amazonMarketplaceId;

    private String sku;

    private String value;

    private Integer status;

    private String uuid;

    private Integer type;

    private String errorInfo;

}
