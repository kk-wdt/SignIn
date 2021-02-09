package com.kktt.jesus.schedule.task;

public class ProductTask extends BaseTask {

    private Integer id;
    private Integer productId;
    private String asin;

    public ProductTask() {
    }

    public ProductTask(Integer id, Integer productId, String asin) {
        this.id = id;
        this.productId = productId;
        this.asin = asin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
