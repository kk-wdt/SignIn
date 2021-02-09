package com.kktt.jesus.schedule.task;


import com.kktt.jesus.dataobject.mws.VariationData;

import java.util.List;

public class ListingRelationTask extends FeedBaseTask {
    private String sku;

    private List<VariationData> variationData;

    public List<VariationData> getVariationData() {
        return variationData;
    }

    public void setVariationData(List<VariationData> variationData) {
        this.variationData = variationData;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
