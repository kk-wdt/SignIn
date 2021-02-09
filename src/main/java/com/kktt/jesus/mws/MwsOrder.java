package com.kktt.jesus.mws;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.orders._2013_09_01.model.*;
import com.kktt.jesus.dataobject.AmazonMarketplace;
import com.tapcash.tool4seller.library.sellercentral.MwsUtil;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Arrays;

@Component
public class MwsOrder {
	
	public ListOrdersResponse getOrdersByDateRange(AmazonMarketplace shopEntity, XMLGregorianCalendar beforeXCalendar, XMLGregorianCalendar afterXCalendar) {
		String marketplaceId = shopEntity.getMarketplaceId();
		String sellerId = shopEntity.getSellerId();
		
		ListOrdersRequest request = new ListOrdersRequest();
		request.setSellerId(sellerId);
		request.setMWSAuthToken(shopEntity.getAuthToken());
		request.setMarketplaceId(Arrays.asList(marketplaceId));
		
		request.setLastUpdatedAfter(afterXCalendar);
		request.setLastUpdatedBefore(beforeXCalendar);
		request.setOrderStatus(Arrays.asList("Pending", "Unshipped", "PartiallyShipped", "Shipped", "Canceled"));
		
		MarketplaceWebServiceOrdersClient client = getOrdersClient(marketplaceId);
		return client.listOrders(request); //处理请求限制，初始6个请求，每分钟1个请求 
	}

	public ListOrdersResponse getOrdersByDateCreateRange(AmazonMarketplace shopEntity, XMLGregorianCalendar beforeXCalendar, XMLGregorianCalendar afterXCalendar) {
		String marketplaceId = shopEntity.getMarketplaceId();
		String sellerId = shopEntity.getSellerId();

		ListOrdersRequest request = new ListOrdersRequest();
		request.setSellerId(sellerId);
		request.setMWSAuthToken(shopEntity.getAuthToken());
		request.setMarketplaceId(Arrays.asList(marketplaceId));

		request.setCreatedAfter(afterXCalendar);
		request.setCreatedBefore(beforeXCalendar);
		request.setOrderStatus(Arrays.asList("Pending", "Unshipped", "PartiallyShipped", "Shipped", "Canceled"));

		MarketplaceWebServiceOrdersClient client = getOrdersClient(marketplaceId);
		return client.listOrders(request); //处理请求限制，初始6个请求，每分钟1个请求
	}
	
	public ListOrdersByNextTokenResponse getOrdersByNextToken(AmazonMarketplace shopEntity, String nextToken) {
		String sellerId = shopEntity.getSellerId();
		
		ListOrdersByNextTokenRequest request = new ListOrdersByNextTokenRequest();
		request.setSellerId(sellerId);
		request.setMWSAuthToken(shopEntity.getAuthToken());
		request.setNextToken(nextToken);
		
		MarketplaceWebServiceOrdersClient client = getOrdersClient(shopEntity.getMarketplaceId());
		return client.listOrdersByNextToken(request); //处理请求限制，初始6个请求，每分钟1个请求
	}
	
	public ListOrderItemsResponse getOrderItems(AmazonMarketplace shopEntity, String amazonOrderId) {
		ListOrderItemsRequest request = new ListOrderItemsRequest();
		request.setSellerId(shopEntity.getSellerId());
		request.setMWSAuthToken(shopEntity.getAuthToken());
		request.setAmazonOrderId(amazonOrderId);
		
		MarketplaceWebServiceOrdersClient client = getOrdersClient(shopEntity.getMarketplaceId());
		return client.listOrderItems(request);
	}
	
	public ListOrderItemsByNextTokenResponse getOrderItemsByNextToken(AmazonMarketplace shopEntity, String nextToken) {
		ListOrderItemsByNextTokenRequest request = new ListOrderItemsByNextTokenRequest();
		request.setSellerId(shopEntity.getSellerId());
		request.setMWSAuthToken(shopEntity.getAuthToken());
		request.setNextToken(nextToken);
		
		MarketplaceWebServiceOrdersClient client = getOrdersClient(shopEntity.getMarketplaceId());
		return client.listOrderItemsByNextToken(request);
	}

	private MarketplaceWebServiceOrdersClient getOrdersClient(String marketplaceId) {
		MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
		config.setServiceURL(MwsUtil.getMWSEndpoint(marketplaceId));
		return new MarketplaceWebServiceOrdersClient(MwsUtil.getAccessKey(marketplaceId), MwsUtil.getSecretKey(marketplaceId), config);
	}
	
}
