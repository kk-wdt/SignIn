package com.kktt.jesus.mws;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsConfig;
import com.amazonservices.mws.products.model.*;
import com.kktt.jesus.dataobject.AmazonMarketplace;
import com.tapcash.tool4seller.library.sellercentral.MwsUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MwsProduct {

    public GetProductCategoriesForASINResponse getProductCategoriesForASIN(AmazonMarketplace amazonMarketplace, String asin) {
        GetProductCategoriesForASINRequest request = new GetProductCategoriesForASINRequest();
        request.setSellerId(amazonMarketplace.getSellerId());
        request.setMWSAuthToken(amazonMarketplace.getAuthToken());
        request.setMarketplaceId(amazonMarketplace.getMarketplaceId());
        request.setASIN(asin);

        MarketplaceWebServiceProductsClient client = getProductsClient(amazonMarketplace.getMarketplaceId());
        return client.getProductCategoriesForASIN(request);
    }

    public GetLowestOfferListingsForASINResponse getLowestOfferListingsForASIN(AmazonMarketplace amazonMarketplace, List<String> listAsins) {
        GetLowestOfferListingsForASINRequest request = new GetLowestOfferListingsForASINRequest();
        ASINListType asinList = new ASINListType();
        asinList.setASIN(listAsins);
        request.setSellerId(amazonMarketplace.getSellerId());
        request.setMWSAuthToken(amazonMarketplace.getAuthToken());
        request.setMarketplaceId(amazonMarketplace.getMarketplaceId());
        request.setASINList(asinList);

        MarketplaceWebServiceProductsClient client = getProductsClient(amazonMarketplace.getMarketplaceId());
        return client.getLowestOfferListingsForASIN(request);
    }

    public GetLowestOfferListingsForSKUResponse getLowestOfferListingsForSKU(AmazonMarketplace amazonMarketplace, List<String> listSKU, boolean excludeMe) {
        GetLowestOfferListingsForSKURequest request = new GetLowestOfferListingsForSKURequest();
        request.setSellerId(amazonMarketplace.getSellerId());
        request.setItemCondition("New");
        //是否排除自身
        request.setExcludeMe(excludeMe);
        request.setMWSAuthToken(amazonMarketplace.getAuthToken());
        request.setMarketplaceId(amazonMarketplace.getMarketplaceId());
        SellerSKUListType sellerSKUListType = new SellerSKUListType();
        sellerSKUListType.setSellerSKU(listSKU);
        request.setSellerSKUList(sellerSKUListType);
        MarketplaceWebServiceProductsClient client = getProductsClient(amazonMarketplace.getMarketplaceId());
        return client.getLowestOfferListingsForSKU(request);
    }

    public GetCompetitivePricingForSKUResponse getCompetitivePricingForSKU(AmazonMarketplace amazonMarketplace, List<String> listSku) {
        GetCompetitivePricingForSKURequest request = new GetCompetitivePricingForSKURequest();
        SellerSKUListType skuList = new SellerSKUListType();
        skuList.setSellerSKU(listSku);
        request.setSellerId(amazonMarketplace.getSellerId());
        request.setMWSAuthToken(amazonMarketplace.getAuthToken());
        request.setMarketplaceId(amazonMarketplace.getMarketplaceId());
        request.setSellerSKUList(skuList);

        MarketplaceWebServiceProductsClient client = getProductsClient(amazonMarketplace.getMarketplaceId());
        return client.getCompetitivePricingForSKU(request);
    }


    public GetMatchingProductForIdResponse getMatchingProductForId(AmazonMarketplace amazonMarketplace, List<String> asinList) {
        GetMatchingProductForIdRequest request = new GetMatchingProductForIdRequest();
        request.setSellerId(amazonMarketplace.getSellerId());
        request.setMWSAuthToken(amazonMarketplace.getAuthToken());
        request.setMarketplaceId(amazonMarketplace.getMarketplaceId());
        request.setIdType("ASIN");

        IdListType idListType = new IdListType();
        idListType.setId(asinList);
        request.setIdList(idListType);

        MarketplaceWebServiceProductsClient client = getProductsClient(amazonMarketplace.getMarketplaceId());
        return client.getMatchingProductForId(request);
    }

    private MarketplaceWebServiceProductsClient getProductsClient(String marketplaceId) {
        MarketplaceWebServiceProductsConfig config = new MarketplaceWebServiceProductsConfig();
        config.setServiceURL(MwsUtil.getMWSEndpoint(marketplaceId));
        return new MarketplaceWebServiceProductsClient(MwsUtil.getAccessKey(marketplaceId), MwsUtil.getSecretKey(marketplaceId),
                "tool4seller", "1.0", config);
    }
}