package com.kktt.jesus.dataobject;

import lombok.Data;

@Data
public class AliexpressSkuEntity {

    private Long skuId;

    private Long productId;

    private String imageUrl;

    private Double price;

    private Integer inventory;

    private String properties;

}
