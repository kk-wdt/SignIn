package com.kktt.jesus.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * goten_product
 * @author 
 */
@Data
public class GotenProductEntity implements Serializable {
    private String id;

    private Long sku;

    private String site;

    /**
     * 0 未处理图片 1处理图片中 2成功 3失败 4处理图片完成 5任务执行中
     */
    private Byte state;

    private String skuImageUrl;

    private String previewImageUrl;

    private String imageUrls;

    private String title;

    private Double price;

    private Integer inventory;

    private String categoryFirstName;

    private String categorySecondName;

    private String categoryThirdName;

    private String categoryThirdCode;

    private String description;

    private String bulletPoint;

    private String keywords;

    private String property;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}