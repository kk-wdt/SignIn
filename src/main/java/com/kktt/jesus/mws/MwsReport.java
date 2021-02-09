package com.kktt.jesus.mws;

import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.*;
import com.kktt.jesus.dataobject.AmazonMarketplace;
import com.kktt.jesus.exception.MWSRequestException;
import com.kktt.jesus.utils.MWSExceptionIdentifyUtil;
import com.tapcash.tool4seller.library.sellercentral.Constant;
import com.tapcash.tool4seller.library.sellercentral.MwsUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class MwsReport {

    private MarketplaceWebServiceClient createMarketplaceWebServiceClient(String marketplaceId) {
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(MwsUtil.getMWSEndpoint(marketplaceId));
        return new MarketplaceWebServiceClient(MwsUtil.getAccessKey(marketplaceId), MwsUtil.getSecretKey(marketplaceId), "tool4seller", "1.0", config);
    }

    /**
     * 获取报表请求ID
     */
    public String requestReportAndGetReportRequestId(AmazonMarketplace amazonMarketplace, RequestReportRequest request) throws MWSRequestException {
        MarketplaceWebServiceClient client = createMarketplaceWebServiceClient(amazonMarketplace.getMarketplaceId());
        try {
            RequestReportResponse requestReportResponse = client.requestReport(request);
            ReportRequestInfo reportRequestInfo = requestReportResponse.getRequestReportResult().getReportRequestInfo();
            return reportRequestInfo.getReportRequestId();
        } catch (MarketplaceWebServiceException e) {
            MWSExceptionIdentifyUtil.throwIt(e, amazonMarketplace);
            throw new MWSRequestException(e.getXML(), amazonMarketplace);
        }
    }

    /**
     * 根据报表请求ID获取报表ID
     */
    public String getReportId(AmazonMarketplace amazonMarketplace, String reportRequestId) throws MWSRequestException {
        GetReportListRequest request = new GetReportListRequest();
        request.setMerchant(amazonMarketplace.getSellerId());
        request.setMWSAuthToken(amazonMarketplace.getAuthToken());
        request.setReportRequestIdList(new IdList(Collections.singletonList(reportRequestId)));

        try {
            MarketplaceWebServiceClient marketplaceWebServiceClient = createMarketplaceWebServiceClient(amazonMarketplace.getMarketplaceId());
            GetReportListResponse response = marketplaceWebServiceClient.getReportList(request);

            List<ReportInfo> reportInfoList = response.getGetReportListResult().getReportInfoList();
            if (reportInfoList == null || reportInfoList.size() == 0) return null;
            String reportId = reportInfoList.get(0).getReportId();
            if (StringUtils.isEmpty(reportId)) return null;
            return reportId;
        } catch (MarketplaceWebServiceException e) {
            MWSExceptionIdentifyUtil.throwIt(e, amazonMarketplace);
            throw new MWSRequestException(e.getXML(), amazonMarketplace);
        }
    }

    public List<Object> getReportRequestStatus(AmazonMarketplace amazonMarketplace, String requestId) throws MWSRequestException {
        GetReportRequestListRequest request = new GetReportRequestListRequest();
        request.setMerchant(amazonMarketplace.getSellerId());
        request.setMWSAuthToken(amazonMarketplace.getAuthToken());
        request.setReportRequestIdList(new IdList(Collections.singletonList(requestId)));

        MarketplaceWebServiceClient client = createMarketplaceWebServiceClient(amazonMarketplace.getMarketplaceId());
        try {
            GetReportRequestListResponse response = client.getReportRequestList(request);
            List<ReportRequestInfo> reportRequestInfoList = response.getGetReportRequestListResult().getReportRequestInfoList();
            ReportStatus reportStatus = ReportStatus.from(reportRequestInfoList.get(0).getReportProcessingStatus());
            String reportId = reportRequestInfoList.get(0).getGeneratedReportId();
            return Arrays.asList(reportStatus, reportId);
        } catch (MarketplaceWebServiceException e) {
            MWSExceptionIdentifyUtil.throwIt(e, amazonMarketplace);
            throw new MWSRequestException(e.getXML(), amazonMarketplace);
        }
    }

    /**
     * 获取报表内容
     */
    public String getReportContent(AmazonMarketplace amazonMarketplace, String reportId) throws MWSRequestException {
        GetReportRequest request = new GetReportRequest();
        request.setMerchant(amazonMarketplace.getSellerId());
        request.setReportId(reportId);
        request.setMWSAuthToken(amazonMarketplace.getAuthToken());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        request.setReportOutputStream(byteArrayOutputStream);

        String marketplaceId = amazonMarketplace.getMarketplaceId();
        MarketplaceWebServiceClient client = createMarketplaceWebServiceClient(marketplaceId);
        try {
            client.getReport(request);
        } catch (MarketplaceWebServiceException e) {
            MWSExceptionIdentifyUtil.throwIt(e, amazonMarketplace);
            throw new MWSRequestException(e.getXML(), amazonMarketplace);
        }

        try {
            if (marketplaceId.equalsIgnoreCase(Constant.MARKETPLACE_ID_JP)) {
                return byteArrayOutputStream.toString("Windows-31J");
            }
            return byteArrayOutputStream.toString("CP1252");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void getReportContentFromFile(AmazonMarketplace amazonMarketplace, String reportId) throws MWSRequestException {
        GetReportRequest request = new GetReportRequest();
        request.setMerchant(amazonMarketplace.getSellerId());
        request.setReportId(reportId);
        request.setMWSAuthToken(amazonMarketplace.getAuthToken());
        File file = new File("C:\\Users\\Admin\\Desktop\\error\\monthInventoryReport.csv");
        try {
            request.setReportOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String marketplaceId = amazonMarketplace.getMarketplaceId();
        MarketplaceWebServiceClient client = createMarketplaceWebServiceClient(marketplaceId);
        try {
            client.getReport(request);
        } catch (MarketplaceWebServiceException e) {
            MWSExceptionIdentifyUtil.throwIt(e, amazonMarketplace);
            throw new MWSRequestException(e.getXML(), amazonMarketplace);
        }
    }

    public String getReportRequestList(AmazonMarketplace amazonMarketplace, String requestId) throws MWSRequestException {
        MarketplaceWebServiceClient client = createMarketplaceWebServiceClient(amazonMarketplace.getMarketplaceId());
        try {
            GetReportRequestListRequest request = new GetReportRequestListRequest();
            request.setMerchant(amazonMarketplace.getSellerId());
            request.setMWSAuthToken(amazonMarketplace.getAuthToken());
            IdList idList = new IdList();
            idList.withId(requestId);
            request.setReportRequestIdList(idList);
            GetReportRequestListResponse requestReportResponse = client.getReportRequestList(request);
            String status = requestReportResponse.getGetReportRequestListResult().getReportRequestInfoList().get(0).getReportProcessingStatus();
            if(status.equalsIgnoreCase("_DONE_")){
                return requestReportResponse.getGetReportRequestListResult().getReportRequestInfoList().get(0).getGeneratedReportId();
            }else if(status.equalsIgnoreCase("_DONE_NO_DATA_")){
                return "_DONE_NO_DATA_";
            }
            else{
                System.out.println("status："+status);
                return null;
            }
        } catch (MarketplaceWebServiceException e) {
            MWSExceptionIdentifyUtil.throwIt(e, amazonMarketplace);
            throw new MWSRequestException(e.getXML(), amazonMarketplace);
        }
    }
}