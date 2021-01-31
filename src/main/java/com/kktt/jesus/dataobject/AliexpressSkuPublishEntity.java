package com.kktt.jesus.dataobject;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AliexpressSkuPublishEntity {
    public interface STATE{
        Integer IGNORE = -1;//不符合要求商品
        Integer UNDO = 0;//未处理图片
        Integer DOING = 1;//处理图片中
        Integer SUCCESS = 2;//创建成功
        Integer FAILURE = 3;//创建失败
        Integer FINISH_IMAGE = 4;//图片处理完成
        Integer REQUEST = 5;//请求创建任务中
    }
    private String id;

    private Integer amazonMarketplaceId;

    private String nodeId;

    private String prefix;

    private Long skuId;

    private Long productId;

    private String site;

    private String upc;

    private Integer count;

    private Integer state;

    private String skuImageUrl;

    private String previewImageUrl;

    private String imageUrls;

    private String title;

    private BigDecimal price;

    private Integer inventory;

    private String properties;

    private String nodeValue;

    private String itemType;

    private String submissionId;

    private String errorCode;

    private String error;

}
