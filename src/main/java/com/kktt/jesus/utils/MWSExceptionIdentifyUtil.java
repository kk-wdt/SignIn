package com.kktt.jesus.utils;

import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonservices.mws.client.MwsException;
import com.kktt.jesus.SpringContext;
import com.kktt.jesus.dataobject.AmazonMarketplace;
import com.kktt.jesus.exception.*;
import com.kktt.jesus.service.AmazonMarketplaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.util.StringUtil;

public class MWSExceptionIdentifyUtil {

    private static final Logger logger = LoggerFactory.getLogger(MWSExceptionIdentifyUtil.class);

    private static void defaultThrowIt(String sellerId, String marketplaceId, int statusCode, String xml, String errorCode, String message) throws MWSRequestThrottledException, MWSAuthTokenInvalidException, MWSTempAccessDeniedException, MWSInvalidParameterValueException {
        xml = StringUtil.isEmpty(xml) ? xml : xml.replace("\n", "");
        if (statusCode == 503) {
            throw new MWSRequestThrottledException(xml, sellerId, marketplaceId);
        }
        if (statusCode == 401 && errorCode.equals("AccessDenied") && message.contains("AuthToken is not valid for SellerId and AWSAccountId")) {
            // Customer cancel mws authorization. / Permanent auth failed.
            throw new MWSAuthTokenInvalidException(xml, sellerId, marketplaceId);
        }
        if (statusCode == 401 && errorCode.equals("AccessDenied") && message.contains("The seller does not have an eligible Amazon account to call Amazon MWS. For more information about eligible accounts, see the Amazon MWS documentation.")) {
            // Customer cancel mws authorization. / Permanent auth failed.
            throw new MWSTempAccessDeniedException(xml, sellerId, marketplaceId);
        }

        if (statusCode == 401 && errorCode.equals("AccessDenied") && message.contains("Access denied")) {
            // Customer selling right removed. / Temp auth failed.
            throw new MWSTempAccessDeniedException(xml, sellerId, marketplaceId);
        }

        // Access to Finances.ListFinancialEventGroups
        // Access to Finances.ListFinancialEventGroups is denied
        if (statusCode == 401 && errorCode.equals("AccessDenied") && message.contains("Access to ") && message.contains(" is denied")) {
            throw new MWSTempAccessDeniedException(xml, sellerId, marketplaceId);
        }
        if (statusCode == 401 && message.contains("does not have access to the given marketplace")) {
            throw new MWSTempAccessDeniedException(xml, sellerId, marketplaceId);
        }
        if (statusCode == 400) {
            throw new MWSInvalidParameterValueException(xml, sellerId, marketplaceId);
        }
    }

    public static void throwIt(MarketplaceWebServiceException e, AmazonMarketplace amazonMarketplace) throws MWSRequestException {
        throwIt(e, amazonMarketplace.getSellerId(), amazonMarketplace.getMarketplaceId());
    }

    private static void throwIt(MarketplaceWebServiceException e, String sellerId, String marketplaceId) throws MWSRequestException {
        defaultThrowIt(sellerId, marketplaceId, e.getStatusCode(), e.getXML(), e.getErrorCode(), e.getMessage());
    }

    public static void throwIt(MwsException e, AmazonMarketplace amazonMarketplace) throws MWSRequestException {
        throwIt(e, amazonMarketplace.getSellerId(), amazonMarketplace.getMarketplaceId());
    }

    private static void throwIt(MwsException e, String sellerId, String marketplaceId) throws MWSRequestException {
        defaultThrowIt(sellerId, marketplaceId, e.getStatusCode(), e.getXML(), e.getErrorCode(), e.getMessage());
    }

    // The seller does not have an eligible Amazon account to call Amazon MWS. For more information about eligible accounts, see the Amazon MWS documentation.
    public static void MWSAuthTokenInvalidExceptionHandler(MWSAuthTokenInvalidException e) {
        SpringContext.getContext().getBean(AmazonMarketplaceService.class).update(e.getSellerId(), e.getMarketplaceId(), -9);
        logger.info("SellerId: {} MarketplaceId: {} 状态切换为 -9, Exception: {}", e.getSellerId(), e.getMarketplaceId(), e.getMessage());
    }

    public static void MWSTempAccessDeniedExceptionHandler(MWSTempAccessDeniedException e) {
        SpringContext.getContext().getBean(AmazonMarketplaceService.class).update(e.getSellerId(), e.getMarketplaceId(), -1);
        logger.info("SellerId: {} MarketplaceId: {} 状态切换为 -1, Exception: {}", e.getSellerId(), e.getMarketplaceId(), e.getMessage());
    }

    public static void MWSConnectedStatusExceptionHandler(MWSConnectedStatusException e) {
        logger.info("SellerId: {} MarketplaceId: {} 状态不正常, 跳过队列操作, Exception: {}", e.getSellerId(), e.getMarketplaceId(), e.getMessage());
    }

    // OtherFee Task: End date is not valid, should be no later than 2 minutes from now
    // OtherFee Task: Token is not valid, token duration is 17 minutes.
    // Report rejected for this client. The provided marketplaces are correctly associated with your account, but you are prevented from performing this action in the following marketplaces: ATVPDKIKX0DER. Please contact Seller Support in your home marketplace for more information about your account.
    public static void MWSInvalidParameterValueExceptionHandler(MWSInvalidParameterValueException e) {
        logger.info("SellerId: {} MarketplaceId: {} 参数异常, 跳过队列操作, Exception: {}", e.getSellerId(), e.getMarketplaceId(), e.getMessage());
    }

    public static void MWSRequestThrottledExceptionHandler(MWSRequestThrottledException e) {
        // 频率太高 不建议打印 有需要的时候开启
        logger.debug("SellerId: {} MarketplaceId: {} Request Throttled, 跳过队列操作, Exception: {}", e.getSellerId(), e.getMarketplaceId(), e.getMessage());
    }

    public static void MWSQueueTaskRetryExceptionHandler(MWSQueueTaskRetryException e) {
        logger.info("SellerId: {} MarketplaceId: {} 请求重试, Exception: {}", e.getSellerId(), e.getMarketplaceId(), e.getMessage());
    }
}