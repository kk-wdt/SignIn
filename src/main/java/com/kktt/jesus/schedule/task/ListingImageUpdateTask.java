package com.kktt.jesus.schedule.task;


public class ListingImageUpdateTask extends FeedBaseTask {

	private String sku;

	private String mainImage;

	private String extraImages;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
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
}
