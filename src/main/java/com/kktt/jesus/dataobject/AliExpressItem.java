package com.kktt.jesus.dataobject;

import lombok.Data;

@Data
public class AliExpressItem {

    private double price;

    private int inventory;

    private String sku;

    public AliExpressItem(){}

    public AliExpressItem(String sku, int inventory) {
        this.sku = sku;
        this.inventory = inventory;
    }

    public AliExpressItem(String sku, double price) {
        this.sku = sku;
        this.price = price;
    }
}
