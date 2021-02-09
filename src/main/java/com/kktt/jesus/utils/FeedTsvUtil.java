package com.kktt.jesus.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.kktt.jesus.dataobject.AmazonsCategoryTemplateEntity;
import com.kktt.jesus.dataobject.mws.ListingInfo;
import com.kktt.jesus.dataobject.mws.VariationData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FeedTsvUtil {
    public static final List<String> VALIDATION_PARENT_EXCEPT = Arrays.asList("external_product_id","external_product_id_type","standard_price","quantity","parent_sku");
    public static final List<String> BASE_PARAMS = Arrays.asList("feed_product_type","item_sku","item_type","item_name","condition_type","manufacturer","external_product_id","external_product_id_type","standard_price","quantity","brand_name","main_image_url");
    protected static final Logger logger = LoggerFactory.getLogger(FeedTsvUtil.class);

    /**
     * @param listingInfo
     * @param templateParamName
     * @param variation
     * @param isVariationParent 是变体并且是父亲
     * @return
     */
    private static String matchBaseTemplateParam(ListingInfo listingInfo, String templateParamName, VariationData variation, boolean isVariationParent, boolean isVariation){
        //父变体不需要填写UPC/价格/库存
        if(isVariationParent){
            if(VALIDATION_PARENT_EXCEPT.contains(templateParamName)){
                return null;
            }
        }
        String result = null;
        switch (templateParamName){
            case "condition_type":
                if(isVariation && !isVariationParent){
                    result = variation.getCondition();
                }else{
                    result = listingInfo.getCondition();
                }
                break;
            case "feed_product_type":
                result = listingInfo.getProductType();
                break;
            case "item_sku":
                if(isVariation&&!isVariationParent){
                    result = variation.getSku();
                }else{
                    result = listingInfo.getSku();
                }
                break;
            case "brand_name":
                result = listingInfo.getBrand();
                break;
            case "item_type":
                result = listingInfo.getItemType();
                if(StringUtils.isEmpty(result)){
                    result = listingInfo.getProductData().getString("item_type");
                }
                break;
            case "item_name":
                result = listingInfo.getTitle();
                break;
            case "external_product_id":
                if(isVariation){
                    result = variation.getStandardProductValue();
                }else{
                    result = listingInfo.getStandardProductValue();
                }
                break;
            case "manufacturer":
                result = listingInfo.getManufacturer();
                break;
            case "external_product_id_type":
                if(isVariation){
                    result = variation.getStandardProductType();
                }else{
                    result = listingInfo.getStandardProductType();
                }
                break;
            case "main_image_url":
                if(isVariation && !isVariationParent){
                    result = variation.getParamData().getString("image");
                }else{
                    result = listingInfo.getMainImage();
                }
                break;
            case "standard_price":
                if(isVariation){
                    result = variation.getPrice()+"";
                }else{
                    result = listingInfo.getStandardPrice()+"";
                }
                break;
            case "quantity":
                if(isVariation){
                    result = variation.getQuantity()+"";
                }else{
                    result = listingInfo.getQuantity()+"";
                }
                break;
            case "update_delete":
                if(listingInfo.getProductData() != null){
                    String updateDelete = listingInfo.getProductData().getString("update_delete");
                    if(StringUtils.isNotEmpty(updateDelete)){
                        return updateDelete;
                    }
                }
                break;
        }
        return result;
    }

    private static String matchChildParam(String param, ListingInfo listingInfo, VariationData variation, boolean isVariation) {
        if(BASE_PARAMS.contains(param)){
            String baseTemplateParamValue = matchBaseTemplateParam(listingInfo,param,variation,false,isVariation);
            if(baseTemplateParamValue != null){
                return baseTemplateParamValue;
            }
        }

        if(isVariation){
            String variationTemplateParamValue = matchVariationTemplateParam(param,variation,listingInfo.getVariationTheme(),listingInfo.getSku());
            if(variationTemplateParamValue != null){
                return variationTemplateParamValue;
            }
        }

        String otherTemplateParamValue = matchOtherTemplateParam(param,listingInfo,isVariation);
        if(otherTemplateParamValue != null){
            return otherTemplateParamValue;
        }

        JSONObject productData = listingInfo.getProductData();
        for (String templateParam : productData.keySet()) {
            if(templateParam.equals(param)){
                return productData.getString(templateParam);
            }
        }
        return null;
    }

    private static String matchOtherTemplateParam(String param, ListingInfo listingInfo, boolean isVariation) {
        String result = null;

        if(param.startsWith("other_image_url")){
//            if(isVariation){
//                return null;
//            }
            String extraImages = listingInfo.getExtraImages();
            if(StringUtils.isNotEmpty(extraImages)){
                if(isVariation){
                    extraImages = listingInfo.getMainImage()+","+extraImages;
                }
                String[] otherImageArr = extraImages.split(",");
                String number = param.substring(param.length() - 1);
                int index = Integer.parseInt(number);
                if(otherImageArr.length >= index){
                    return otherImageArr[index-1];
                }
            }
            return null;
        }

        if(param.startsWith("bullet_point")){
            String bulletPoints = listingInfo.getBulletPoints();
            List<String> bulletPointList = JSON.parseObject(bulletPoints,new TypeReference<List<String>>(){});
            if(CollectionUtils.isNotEmpty(bulletPointList)){
                if(param.equals("bullet_point")){
                    return bulletPointList.get(0);
                }
                int length = "bullet_point".length();
                String number = param.substring(length);
//                String number = param.substring(param.length() - 1);
                int index = Integer.parseInt(number);
                if(bulletPointList.size() >= index){
                    return bulletPointList.get(index - 1);
                }
            }
            return null;
        }

        if(param.startsWith("generic_keywords")){
            JSONObject productData = listingInfo.getProductData();
            if(productData != null){
                return productData.getString("generic_keywords");
            }
            return null;
        }

        if ("product_description".equals(param)) {
            result = listingInfo.getDescription();
        }
        return result;
    }

    private static String matchVariationTemplateParam(String templateParamName, VariationData variationData,String variationTheme,String parentSku) {
        String result = null;
        switch (templateParamName){
            case "variation_theme":
                result = variationTheme;
                break;
            case "parent_child":
                result = "child";
                break;
            case "parent_sku":
                result = parentSku;
                break;
            case "relationship_type":
                result = "Variation";
                break;
        }

        //变体主题动态参数
        JSONObject paramData = variationData.getParamData();
        if(paramData == null){
            logger.error("listing搬运异常：变体信息缺失 sku：{}",variationData.getSku());
            throw new IllegalArgumentException("变体信息缺失");
        }
        for (String key : paramData.keySet()) {
            if(key.equals(templateParamName)){
                result = paramData.getString(key);
            }
        }
        return result;
    }

    private static StringBuilder addVariationParent(ListingInfo listingInfo, List<String> paramList) {
        StringBuilder content = new StringBuilder();
        for (String templateParamName : paramList) {
            String templateParamValue = matchBaseTemplateParam(listingInfo,templateParamName,null,true,true);
            if(templateParamValue != null){
                content.append(templateParamValue).append("\t");
            }else{
                if(templateParamName.equals("parent_child")){
                    content.append("parent").append("\t");
                }else if(templateParamName.equals("variation_theme")){
                    content.append(listingInfo.getVariationTheme()).append("\t");
                }else{
                    //父体需要拼接5点描述和附图
                    String otherTemplateParamValue = matchOtherTemplateParam(templateParamName,listingInfo,false);
                    if(otherTemplateParamValue != null){
                        content.append(otherTemplateParamValue).append("\t");
                    }else{
                        content.append("\t");
                    }
                }
            }
        }
        return content;
    }

    public static String createRequestContent(ListingInfo listingInfo,AmazonsCategoryTemplateEntity template){
//        StringBuilder header = new StringBuilder(template.getTemplate());
//        String paramsJson = template.getParams();
//        List<String> paramList = JSON.parseObject(paramsJson, new TypeReference<List<String>>() {});
//        StringBuilder content = createSingleContent(listingInfo, paramList);
//        header.append(content);
        return batchCreateRequestContent(Collections.singletonList(listingInfo),template);
    }

    public static String batchCreateRequestContent(List<ListingInfo> listingInfos, AmazonsCategoryTemplateEntity template){
        StringBuilder header = new StringBuilder(template.getTemplate());
        String paramsJson = template.getParams();
        List<String> paramList = JSON.parseObject(paramsJson, new TypeReference<List<String>>() {});
        for (ListingInfo listingInfo : listingInfos) {
            String desc = listingInfo.getDescription();
            if(StringUtils.isNotEmpty(desc)){
                desc = desc.replaceAll("\r\n|\r|\n", "");
            }
            listingInfo.setDescription(desc);
            StringBuilder content = createSingleContent(listingInfo, paramList);
            header.append(content);
        }
        return header.toString();
    }

    private static StringBuilder createSingleContent(ListingInfo listingInfo, List<String> paramList){
        StringBuilder content = new StringBuilder();
        if(listingInfo.getIsVariation() == 1){
            //添加变体父亲
            StringBuilder validationParent = addVariationParent(listingInfo, paramList);
            content.append(validationParent).append("\n");
            //添加子变体
            List<VariationData> variationData = listingInfo.getVariationData();
            for (VariationData variation : variationData) {
                StringBuilder lineContent = new StringBuilder();
                for (String param : paramList) {
                    String value = matchChildParam(param,listingInfo,variation,true);
                    if(value == null){
                        lineContent.append("\t");
                    }else{
                        lineContent.append(value).append("\t");
                    }
                }
                lineContent.append("\n");
                content.append(lineContent);
            }
        }else{
            StringBuilder lineContent = new StringBuilder();
            for (String param : paramList) {
                String value = matchChildParam(param,listingInfo,null,false);
                if(value == null){
                    lineContent.append("\t");
                }else{
                    lineContent.append(value).append("\t");
                }
            }
            content.append(lineContent).append("\n");
        }
        return content;
    }

}
