package com.kktt.jesus.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kktt.jesus.dataobject.AliExpressItem;
import com.kktt.jesus.dataobject.mws.*;
import com.kktt.jesus.schedule.task.ListingImageUpdateTask;
import com.kktt.jesus.schedule.task.ListingRelationTask;
import com.tapcash.tool4seller.library.sellercentral.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.dom.DOMText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedsXmlUtil {
    private static final Logger logger = LoggerFactory.getLogger(FeedsXmlUtil.class);

    /**
     * 解析XML 转成对象
     * @return
     */
    public static ProcessingSummary parseResponseXml(InputStream inputStream){
        try {
//            File f = new File("C:\\Users\\Admin\\Desktop\\report\\156497018362.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            String submissionId = doc.getElementsByTagName("DocumentTransactionID").item(0).getFirstChild().getNodeValue();
            String messagesProcessed = doc.getElementsByTagName("MessagesProcessed").item(0).getFirstChild().getNodeValue();
            String messagesSuccessful = doc.getElementsByTagName("MessagesSuccessful").item(0).getFirstChild().getNodeValue();
            String messagesWithError = doc.getElementsByTagName("MessagesWithError").item(0).getFirstChild().getNodeValue();
            String messagesWarning = doc.getElementsByTagName("MessagesWithWarning").item(0).getFirstChild().getNodeValue();

            ProcessingSummary summary = new ProcessingSummary(new BigInteger(messagesProcessed),new BigInteger(messagesSuccessful),
                    new BigInteger(messagesWithError),new BigInteger(messagesWarning));
            summary.setSubmissionId(submissionId);
            logger.info("messagesProcessed:{},messagesWithError:{},messagesSuccessful:{}",messagesProcessed,messagesWithError,messagesSuccessful);
            if("0".equals(messagesWithError)){
                return summary;
            }
            List<Result> results = new ArrayList<>();
            NodeList nl = doc.getElementsByTagName("Result");
            for (int i = 0; i < nl.getLength(); i++) {
                Result result = new Result();
                Node node = nl.item(i);//result节点
                NodeList childNodes = node.getChildNodes();
                Map<String, String> resultMap = new HashMap<>();
                for (int j = 0; j <childNodes.getLength() ; j++) {
                    if (childNodes.item(j).getNodeType()== Node.ELEMENT_NODE) {
                        resultMap.put(childNodes.item(j).getNodeName(),childNodes.item(j).getFirstChild().getNodeValue());
                        if("AdditionalInfo".equals(childNodes.item(j).getNodeName())){
                            Node cnode = childNodes.item(j);//result节点
                            NodeList cNodes = cnode.getChildNodes();
                            for (int k = 0; k <cNodes.getLength() ; k++) {
                                if (childNodes.item(k).getNodeType()== Node.ELEMENT_NODE) {
                                    resultMap.put(cNodes.item(k).getNodeName(),cNodes.item(k).getFirstChild().getNodeValue());
                                }
                            }
                        }

                    }
                }
                result.setMessageID(resultMap.get("MessageID"));
                result.setResultCode(resultMap.get("ResultCode"));
                result.setResultMessageCode(resultMap.get("ResultMessageCode"));
                result.setResultDescription(resultMap.get("ResultDescription"));
                result.setSKU(resultMap.get("SKU"));
                results.add(result);
            }
            summary.setResults(results);
            return summary;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProcessingSummary();
    }

    public static String createPriceFeedXml(String sellerId, List<Price> prices) {
        if (CollectionUtils.isEmpty(prices)) {
            return "";
        }
        PriceAmazonEnvelope envelop = new PriceAmazonEnvelope();
        Header header = createHeader(sellerId);
        envelop.setMessageType("Price");
        envelop.setHeader(header);

        List<PriceMessage> messages = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            PriceMessage msg = new PriceMessage();
            BigInteger messageID = new BigInteger(i + 1 + "");
            msg.setMessageID(messageID);
            msg.setPrice(prices.get(i));
            messages.add(msg);
        }
        envelop.setMessages(messages);
        return convent2Xml(envelop, PriceAmazonEnvelope.class);
    }

    /**
     * update inventory
     *
     * @param sellerId
     * @param inventories
     */
    public static String createInventoryQuantityFeedXml(String sellerId, List<Inventory> inventories) {
        if (CollectionUtils.isEmpty(inventories)) {
            return "";
        }
        InventoryAmazonEnvelope envelop = commonInventoryEnvelope(sellerId);

        List<InventoryMessage> messages = new ArrayList<>();
        for (int i = 0; i < inventories.size(); i++) {
            InventoryMessage msg = new InventoryMessage();
            BigInteger messageID = new BigInteger(i + 1 + "");
            msg.setMessageID(messageID);
            msg.setOperationType("Update");
            //重新设置 防止传入不必要的参数
            Inventory inventory = new Inventory();
            inventory.setSku(inventories.get(i).getSku());
            inventory.setQuantity(inventories.get(i).getQuantity());
            inventory.setFulfillmentLatency(inventories.get(i).getFulfillmentLatency());
            msg.setInventory(inventory);
            messages.add(msg);
        }
        envelop.setMessages(messages);
        return convent2Xml(envelop, InventoryAmazonEnvelope.class);
    }

    public static  org.dom4j.Document createDeleteListingXml(String sellerId, List<AliExpressItem> items){
        org.dom4j.Document doc =  DocumentHelper.createDocument();
        Element amazonEnvelope = createBaseXml(sellerId,doc, "Product");
        for (int i = 0; i < items.size(); i++) {
            AliExpressItem item = items.get(i);
            Element message = amazonEnvelope.addElement("Message");
            createElementWithValue(message,"MessageID",(i+1)+"");
            createElementWithValue(message,"OperationType","Delete");
            Element product = message.addElement("Product");
            createElementWithValue(product,"SKU", item.getSku());
        }
        return doc;
    }

    public static org.dom4j.Document createImageUpdateXml(String sellerId, ListingImageUpdateTask listingInfo){
        org.dom4j.Document doc =  DocumentHelper.createDocument();
        Element amazonEnvelope = createBaseXml(sellerId,doc, "ProductImage");
        if(StringUtils.isNotEmpty(listingInfo.getMainImage())){
            createImageMessage(amazonEnvelope,listingInfo.getSku(),listingInfo.getMainImage(),"Main", 1);
        }
        if(StringUtils.isNotEmpty(listingInfo.getExtraImages())){
            String[] extraImageList = listingInfo.getExtraImages().split(",");
            for (int i = 0; i < extraImageList.length; i++) {
                String type = "PT"+(i+1);
                createImageMessage(amazonEnvelope,listingInfo.getSku(),extraImageList[i],type,i+2);
            }
        }
        return doc;
    }

    private static void createImageMessage(Element amazonEnvelope, String sku, String imageUrl, String type, int i){
        Element messageElement = amazonEnvelope.addElement("Message");
        createElementWithValue(messageElement,"MessageID",i+"");
        createElementWithValue(messageElement,"OperationType","Update");
        Element productImageElement = messageElement.addElement("ProductImage");
        createElementWithValue(productImageElement,"SKU",sku);
        createElementWithValue(productImageElement,"ImageType",type);
        createElementWithValue(productImageElement,"ImageLocation",imageUrl);
    }

    public static org.dom4j.Document createRelationXml(String sellerId, ListingRelationTask relationTask){
        org.dom4j.Document doc =  DocumentHelper.createDocument();
        Element amazonEnvelope = createBaseXml(sellerId,doc, "Relationship");
        Element purgeAndReplace = amazonEnvelope.addElement("PurgeAndReplace");
        purgeAndReplace.add(new DOMText("false"));
        List<VariationData> variationData = relationTask.getVariationData();
        for (int i = 0; i < variationData.size(); i++) {
            createRelationMessage(amazonEnvelope,relationTask.getSku(),variationData.get(i).getSku(),i+1);
        }
        return doc;
    }

    private static void createRelationMessage(Element amazonEnvelope,String sku,String childSku,int i){
        Element messageElement = amazonEnvelope.addElement("Message");
        createElementWithValue(messageElement,"MessageID",i+"");
        createElementWithValue(messageElement,"OperationType","Update");
        Element relationShipElement = messageElement.addElement("Relationship");
        createElementWithValue(relationShipElement,"ParentSKU",sku);
        Element relationElement = relationShipElement.addElement("Relation");
        createElementWithValue(relationElement,"SKU",childSku);
        createElementWithValue(relationElement,"Type","Variation");

    }

    private static Element createBaseXml(String sellerId, org.dom4j.Document doc, String messageTypeValue){
        doc.setXMLEncoding("iso-8859-1");
        Element amazonEnvelope = doc.addElement("AmazonEnvelope");
        Element header = amazonEnvelope.addElement("Header");
        Element documentVersion = header.addElement("DocumentVersion");
        documentVersion.add(new DOMText("1.01"));
        Element merchantIdentifier = header.addElement("MerchantIdentifier");
        merchantIdentifier.add(new DOMText(sellerId));

        Element messageType = amazonEnvelope.addElement("MessageType");
        messageType.add(new DOMText(messageTypeValue));
        return amazonEnvelope;
    }

    private static void setElementValue(Element childElement, String mixValue, ProductParam productParam) {
        String attribute = productParam.getAttribute();
        String[] mixArr = mixValue.split("#");
        if(StringUtils.isNotEmpty(attribute)){
            JSONObject attributeJson = JSON.parseObject(attribute);
            String attributeName = attributeJson.getString("paramName");
            childElement.addAttribute(attributeName,mixArr[1]);
        }
        childElement.setText(mixArr[0]);
    }

    private static void createElementWithValue(Element parentElement,String elementName,String value){
        Element element = parentElement.addElement(elementName);
        element.add(new DOMText(value));
    }

    public static InventoryAmazonEnvelope commonInventoryEnvelope(String sellerId) {
        InventoryAmazonEnvelope envelop = new InventoryAmazonEnvelope();
        Header header = createHeader(sellerId);
        envelop.setMessageType("Inventory");
        envelop.setHeader(header);
        return envelop;
    }

    public static Header createHeader(String sellerId) {
        Header header = new Header();
        header.setDocumentVersion(new BigDecimal("1.01"));
        header.setMerchantIdentifier(sellerId);
        return header;
    }

    public static String convent2Xml(Object obj, Class clazz) {
        String str = null;
        try {
            str = beanToXml(obj, clazz);
            logger.debug("requestXml{}:\n", str.replace("><", ">\n<"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * java对象转换为xml文件
     * @param load    java对象.Class
     * @return    xml文件的String
     * @throws JAXBException
     */
    public static String beanToXml(Object obj, Class<?> load) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(load);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }

    public static InputStream conventToStream(String str, String xmlPath, String marketplaceId) {
        logger.debug("requestXml{}:\n", str.replace("><", ">\n<"));
        String charset = "";
        if(Constant.MARKETPLACE_ID_JP.equals(marketplaceId)){
            charset = "Shift_JIS";
        }else{
            charset = "UTF-8";
        }

        File file = new File(xmlPath);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
            }

            OutputStream out = new FileOutputStream(file);
            OutputStreamWriter isr=new OutputStreamWriter(out,charset);
            BufferedWriter bfw = new BufferedWriter(isr);
            bfw.write(str);
            bfw.close();
            return new FileInputStream(xmlPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream conventToStream(String str, String xmlPath) {
        return conventToStream(str,xmlPath,"");
    }

}
