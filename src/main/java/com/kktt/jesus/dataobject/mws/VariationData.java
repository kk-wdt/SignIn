package com.kktt.jesus.dataobject.mws;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

public class VariationData {

    private String sku;

    private String standardProductType;

    private String standardProductValue;

    private String condition;

    private BigDecimal price;

    private Integer quantity;

    private JSONObject paramData;//json

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public JSONObject getParamData() {
        return paramData;
    }

    public void setParamData(JSONObject paramData) {
        this.paramData = paramData;
    }
}
