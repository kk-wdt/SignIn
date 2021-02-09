package com.kktt.jesus.dataobject.mws;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.List;

public class ListingInfo {

    //----------------------------------分类信息------------------------------------------------
    private byte isVariation;//是否变体

    private String path;//分类节点路径

    private String categoryType;

    private String productType;

    private String itemType;

    private String recommendedBrowseNode;

    //----------------------------------产品信息------------------------------------------------
    private String sku;

    private String standardProductType;

    private String standardProductValue;

    private String title;

    private String brand;

    private String manufacturer;

    private String condition;

    //----------------------------------图片信息------------------------------------------------
    private String mainImage;//主图

    private String extraImages;//其他图片

    //----------------------------------价格信息------------------------------------------------
    private BigDecimal standardPrice;

    private Integer quantity;

    //----------------------------------描述信息------------------------------------------------
    private String bulletPoints;

    private String description;

    //----------------------------------属性信息------------------------------------------------
    private JSONObject productData;//json

    //----------------------------------变体信息------------------------------------------------
    private String variationTheme;

    private List<VariationData> variationData;

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getStandardProductType() {
        return standardProductType;
    }

    public void setStandardProductType(String standardProductType) {
        this.standardProductType = standardProductType;
    }

    public String getStandardProductValue() {
        return standardProductValue;
    }

    public void setStandardProductValue(String standardProductValue) {
        this.standardProductValue = standardProductValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBulletPoints() {
        return bulletPoints;
    }

    public void setBulletPoints(String bulletPoints) {
        this.bulletPoints = bulletPoints;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getRecommendedBrowseNode() {
        return recommendedBrowseNode;
    }

    public void setRecommendedBrowseNode(String recommendedBrowseNode) {
        this.recommendedBrowseNode = recommendedBrowseNode;
    }

    public JSONObject getProductData() {
        return productData;
    }

    public void setProductData(JSONObject productData) {
        this.productData = productData;
    }

    public List<VariationData> getVariationData() {
        return variationData;
    }

    public void setVariationData(List<VariationData> variationData) {
        this.variationData = variationData;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getExtraImages() {
        return extraImages;
    }

    public void setExtraImages(String extraImages) {
        this.extraImages = extraImages;
    }

    public BigDecimal getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(BigDecimal standardPrice) {
        this.standardPrice = standardPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public byte getIsVariation() {
        return isVariation;
    }

    public void setIsVariation(byte isVariation) {
        this.isVariation = isVariation;
    }

    public String getVariationTheme() {
        return variationTheme;
    }

    public void setVariationTheme(String variationTheme) {
        this.variationTheme = variationTheme;
    }

}
