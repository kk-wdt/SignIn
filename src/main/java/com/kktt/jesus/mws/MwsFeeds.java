package com.kktt.jesus.mws;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.*;
import com.kktt.jesus.dataobject.AmazonMarketplace;
import com.tapcash.tool4seller.library.sellercentral.Constant;
import com.tapcash.tool4seller.library.sellercentral.MwsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

@Component
public class MwsFeeds {
    private static final Logger LOGGER = LoggerFactory.getLogger(MwsFeeds.class);
    //执行中
    public static final String IN_PROGRESS = "_IN_PROGRESS_";
    //执行完成
    public static final String DONE = "_DONE_";

    public interface SWITCH_FULFILLMENT_TO {
        String MFN = "MFN";
        String AFN = "AFN";
    }

    public static final String INVENTORY_FEED_TYPE = "_POST_INVENTORY_AVAILABILITY_DATA_";
    public static final String PRICING_FEED_TYPE = "_POST_PRODUCT_PRICING_DATA_";
    public static final String PRODUCT_IMAGE_FEED_TYPE = "_POST_PRODUCT_IMAGE_DATA_";

    public static final String PRODUCT_CREATE = "_POST_PRODUCT_DATA_";
    public static final String PRODUCT_RELATIONSHIP = "_POST_PRODUCT_RELATIONSHIP_DATA_";

    public static final String FLAT_FILE_LISTINGS_FEED = "_POST_FLAT_FILE_LISTINGS_DATA_";

    /**
     * 上传数据
     *
     * @param amazonMarketplace
     * @param file
     * @return FeedSubmissionId 用于定期检查上传数据的状态
     */
    public String submitFeed(AmazonMarketplace amazonMarketplace, File file, String type) throws MarketplaceWebServiceException {
        try {
            InputStream inputStream = new FileInputStream(file);
            return submitFeed(amazonMarketplace, inputStream, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String MARKETPLACE_ID_JP = "A1VC38T7YXB528";

    public String submitFeed(AmazonMarketplace amazonMarketplace, InputStream source, String type) throws MarketplaceWebServiceException {
        SubmitFeedRequest request = createRequest(amazonMarketplace, type, source);
        //解决上传listing乱码问题
        if(MwsFeeds.FLAT_FILE_LISTINGS_FEED.equals(type)){
            String marketplaceId = amazonMarketplace.getMarketplaceId();
            if(MARKETPLACE_ID_JP.equals(marketplaceId)){
                request.setContentType(ContentType.TextTabShiftJis);
            }else{
                request.setContentType(ContentType.TextTabUtf8);
            }
        }else{
            request.setContentType(ContentType.TextXml);
        }
        return submitFeed(amazonMarketplace,request);
    }

    public String submitFeed(AmazonMarketplace amazonMarketplace,SubmitFeedRequest request) throws MarketplaceWebServiceException {
        MarketplaceWebService service = getService(amazonMarketplace.getMarketplaceId());
        SubmitFeedResponse response = service.submitFeedFromFile(request);
        if (response.isSetSubmitFeedResult()) {
            SubmitFeedResult submitFeedResult = response.getSubmitFeedResult();
            if (submitFeedResult.isSetFeedSubmissionInfo()) {
                FeedSubmissionInfo feedSubmissionInfo = submitFeedResult.getFeedSubmissionInfo();
                if (feedSubmissionInfo.isSetFeedSubmissionId()) {
                    return feedSubmissionInfo.getFeedSubmissionId();
                }
            }
        }
        return "";
    }


    /**
     * 异步 同时上传多个信息
     *
     * @param marketPlaceId
     * @param requests
     */
    public void submitFeedAsy(String marketPlaceId, List<SubmitFeedRequest> requests) {
        MarketplaceWebService service = getAsyService(marketPlaceId);

        List<Future<SubmitFeedResponse>> responses = new ArrayList();
        Iterator i$ = requests.iterator();

        while (i$.hasNext()) {
            SubmitFeedRequest request = (SubmitFeedRequest) i$.next();
            responses.add(service.submitFeedAsync(request));
        }

        i$ = responses.iterator();

        while (i$.hasNext()) {
            Future future = (Future) i$.next();

            while (!future.isDone()) {
                Thread.yield();
            }

            try {
                SubmitFeedResponse response = (SubmitFeedResponse) future.get();
                SubmitFeedRequest originalRequest = (SubmitFeedRequest) requests.get(responses.indexOf(future));
                System.out.println("Response request id: " + response.getResponseMetadata().getRequestId());
                System.out.println(response.getResponseHeaderMetadata());
                System.out.println();
            } catch (Exception var7) {
                if (var7.getCause() instanceof MarketplaceWebServiceException) {
                    MarketplaceWebServiceException exception = (MarketplaceWebServiceException) MarketplaceWebServiceException.class.cast(var7.getCause());
                    LOGGER.error("err_msg:{};Response Status Code:{};Error Code:{};Error Type:{};Request ID:{};XML:{};ResponseHeaderMetadata:{}", exception.getMessage(), exception.getStatusCode(),
                            exception.getErrorCode(), exception.getErrorType(), exception.getRequestId(), exception.getXML(), exception.getResponseHeaderMetadata());
                } else {
                    var7.printStackTrace();
                }
            }
        }

    }

    public void submitFeedAsy(AmazonMarketplace amazonMarketplace, List<SubmitFeedRequest> requests) {
        submitFeedAsy(amazonMarketplace.getMarketplaceId(), requests);
    }

    public void getAllFeedSubmissionList(AmazonMarketplace amazonMarketplace) {
        getFeedSubmissionList(amazonMarketplace.getMarketplaceId(), amazonMarketplace.getSellerId(), amazonMarketplace.getAuthToken());
    }

    public String getSingleFeedSubmissionList(AmazonMarketplace amazonMarketplace, String submissionId) throws MarketplaceWebServiceException {
        return parseFeedSubmissionListResponse(getFeedSubmissionList(Arrays.asList(submissionId), amazonMarketplace.getMarketplaceId(), amazonMarketplace.getSellerId(), amazonMarketplace.getAuthToken()));
    }

    public GetFeedSubmissionListResponse getFeedSubmissionList(AmazonMarketplace amazonMarketplace, List<String> submissionIds) throws MarketplaceWebServiceException {
        return getFeedSubmissionList(submissionIds, amazonMarketplace.getMarketplaceId(), amazonMarketplace.getSellerId(), amazonMarketplace.getAuthToken());
    }


    /**
     * 检查上传数据的完成情况
     * _IN_PROGRESS_ 处理中
     * _DONE_    处理完成
     *
     * @param marketPlaceId
     */
    private void getFeedSubmissionList(String marketPlaceId, String merchantId, String token) {
        try {
            MarketplaceWebService service = getService(marketPlaceId);
            GetFeedSubmissionListRequest request = getFeedSubmissionListRequest(merchantId, token);
            GetFeedSubmissionListResponse response = service.getFeedSubmissionList(request);
            //获取结果状态
            parseFeedSubmissionListResponse(response);
        } catch (MarketplaceWebServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单个任务的完成情况
     *
     * @param marketPlaceId
     * @param merchantId
     * @return
     */
    private GetFeedSubmissionListResponse getFeedSubmissionList(List<String> submissionIds, String marketPlaceId, String merchantId, String token) throws MarketplaceWebServiceException {
        MarketplaceWebService service = getService(marketPlaceId);
        GetFeedSubmissionListRequest request = getFeedSubmissionListRequest(merchantId, token);
        request.setFeedSubmissionIdList(new IdList(submissionIds));
        GetFeedSubmissionListResponse response = service.getFeedSubmissionList(request);
        //获取结果状态
        return response;
    }


    /**
     * 接收处理报告
     * 该处理报告会指明上传数据中的哪些记录已成功处理，而哪些记录已生成错误
     *
     * @param marketPlaceId
     * @param submissionId
     * @param merchantId
     */
    public String getFeedSubmissionResult(String marketPlaceId, String submissionId, String merchantId, String token) throws MarketplaceWebServiceException {
        MarketplaceWebService service = getService(marketPlaceId);
        GetFeedSubmissionResultRequest request = new GetFeedSubmissionResultRequest();
        request.setMWSAuthToken(token);
        request.setMerchant(merchantId);
        request.setFeedSubmissionId(submissionId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //设置报告的保存路径
        request.setFeedSubmissionResultOutputStream(baos);

        GetFeedSubmissionResultResponse getFeedSubmissionResultResponse = service.getFeedSubmissionResult(request);
        System.out.println(getFeedSubmissionResultResponse.getResponseMetadata().toString());
        try {
            if (marketPlaceId.equalsIgnoreCase(Constant.MARKETPLACE_ID_JP)) {
                return baos.toString("Windows-31J");
            }
            return baos.toString("CP1252");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFeedSubmissionResult(AmazonMarketplace amazonMarketplace, String submissionId) throws MarketplaceWebServiceException {
        return getFeedSubmissionResult(amazonMarketplace.getMarketplaceId(), submissionId, amazonMarketplace.getSellerId(), amazonMarketplace.getAuthToken());
    }

    private String parseFeedSubmissionListResponse(GetFeedSubmissionListResponse response) {
        //获取结果状态
        if (response.isSetGetFeedSubmissionListResult()) {
            GetFeedSubmissionListResult getFeedSubmissionListResult = response.getGetFeedSubmissionListResult();
            List<FeedSubmissionInfo> feedSubmissionInfoList = getFeedSubmissionListResult.getFeedSubmissionInfoList();
            for (FeedSubmissionInfo feedSubmissionInfo : feedSubmissionInfoList) {
                if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
                    //已完成
                    if (DONE.equals(feedSubmissionInfo.getFeedProcessingStatus())) {
                        LOGGER.info("状态:{}--feedSubmissionId:{}", feedSubmissionInfo.getFeedProcessingStatus(), feedSubmissionInfo.getFeedSubmissionId());
                        //进行中
                    } else if (IN_PROGRESS.equals(feedSubmissionInfo.getFeedProcessingStatus())) {
                        LOGGER.info("状态:{}--feedSubmissionId:{}", feedSubmissionInfo.getFeedProcessingStatus(), feedSubmissionInfo.getFeedSubmissionId());
                    } else {
                        LOGGER.error("未知状态{}--feedSubmissionId:{}", feedSubmissionInfo.getFeedProcessingStatus(), feedSubmissionInfo.getFeedSubmissionId());
                    }
                    return feedSubmissionInfo.getFeedProcessingStatus();
                }
            }
        }
        return "";
    }

    private GetFeedSubmissionListRequest getFeedSubmissionListRequest(String merchantId, String token) {
        GetFeedSubmissionListRequest request = new GetFeedSubmissionListRequest();
        request.setMerchant(merchantId);
        request.setMWSAuthToken(token);
        return request;
    }

    private SubmitFeedRequest createRequest(AmazonMarketplace amazonMarketplace, String feedType, InputStream inputStream) {
        String merchantId = amazonMarketplace.getSellerId();
        String sellerDevAuthToken = amazonMarketplace.getAuthToken();
        IdList marketplaces = new IdList(Arrays.asList(amazonMarketplace.getMarketplaceId()));
        SubmitFeedRequest request = new SubmitFeedRequest();
        request.setFeedContent(inputStream);
        request.setMWSAuthToken(sellerDevAuthToken);
        request.setMerchant(merchantId);
        request.setMarketplaceIdList(marketplaces);
        request.setFeedType(feedType);
        return request;
    }

    private MarketplaceWebService getService(String marketPlaceId) {
        String accessKeyId = MwsUtil.getAccessKey(marketPlaceId);
        String secretAccessKey = MwsUtil.getSecretKey(marketPlaceId);
        MarketplaceWebServiceConfig config = getCommonConfig(marketPlaceId);
        return new MarketplaceWebServiceClient(accessKeyId, secretAccessKey, "tool4seller", "1.0", config);
    }

    private MarketplaceWebService getAsyService(String marketPlaceId) {
        String accessKeyId = MwsUtil.getAccessKey(marketPlaceId);
        String secretAccessKey = MwsUtil.getSecretKey(marketPlaceId);
        MarketplaceWebServiceConfig config = getCommonConfig(marketPlaceId);
        config.setServiceURL(MwsUtil.getMWSEndpoint(marketPlaceId));
        config.setMaxAsyncThreads(35);
        return new MarketplaceWebServiceClient(accessKeyId, secretAccessKey, "tool4seller", "1.0", config);
    }

    private MarketplaceWebServiceConfig getCommonConfig(String marketPlaceId) {
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(MwsUtil.getMWSEndpoint(marketPlaceId));
        return config;
    }


}
