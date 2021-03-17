package com.kktt.jesus.dataobject;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * goten_product
 * @author 
 */
@Data
public class GotenProduct implements Serializable {
    public interface STATE{
        Integer NEW = 0;
        Integer UPDATING = 1;
        Integer COMPLETE = 2;
        Integer PUBLISHED = 3;
        Integer DELETE = -8;
    }

    @javax.persistence.Id
    @KeySql(useGeneratedKeys = true)
    private String id;

    private Long sku;

    private String site;

    /**
     * 0 未处理图片 1处理图片中 2成功 3失败 4处理图片完成 5任务执行中
     */
    private Byte state;

    private String skuImageUrl;

    private String imageUrls;

    private String title;
    @Column(name = "price",insertable = false)
    private Double price;
    @Column(name = "inventory",insertable = false)
    private Integer inventory;
    @Column(name = "category_first_name")
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