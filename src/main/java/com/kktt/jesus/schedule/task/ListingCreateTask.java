package com.kktt.jesus.schedule.task;


import com.kktt.jesus.dataobject.mws.ListingInfo;

public class ListingCreateTask extends FeedBaseTask{

	private ListingInfo listingInfo;


	public ListingInfo getListingInfo() {
		return listingInfo;
	}

	public void setListingInfo(ListingInfo listingInfo) {
		this.listingInfo = listingInfo;
	}

}
