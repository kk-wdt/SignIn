package com.kktt.jesus.dataobject;

import lombok.Data;

@Data
public class AliexpressListingEntity {

    private Long productId;

    private String previewImageUrl;

    private String imageUrls;

    private String title;

    private String keywords;

    private String description;

    private String brand;

    private String shippingMethod;

    private Double shippingFee;

    private Double minPrice;

    private Double maxPrice;

    private Integer sold;

    private Integer rating;

    private Integer sync;

    private Integer identifyBrand;

    private Double brandInImagesRate;


}
