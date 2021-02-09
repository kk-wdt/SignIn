//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2020.04.09 ʱ�� 01:59:11 PM CST 
//


package com.kktt.jesus.dataobject.mws;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sku",
    "fulfillmentCenterID",
    "quantity",
    "fulfillmentLatency",
    "lookup",
    "switchFulfillmentTo"
})
@XmlRootElement(name = "Inventory")
public class Inventory {

    @XmlElement(name = "SKU", required = true)
    protected String sku;
    @XmlElement(name = "FulfillmentCenterID", required = true)
    protected String fulfillmentCenterID;

    @XmlElement(name = "Quantity", required = true)
    protected Integer quantity;
    @XmlElement(name = "FulfillmentLatency", required = true)
    protected String fulfillmentLatency;

    @XmlElement(name = "Lookup", required = true)
    protected String lookup;

    @XmlElement(name = "SwitchFulfillmentTo", required = true)
    protected String switchFulfillmentTo;

    public Inventory() {
    }

    public Inventory(String sku, Integer quantity, String fulfillmentLatency) {
        this.sku = sku;
        this.quantity = quantity;
        this.fulfillmentLatency = fulfillmentLatency;
    }
    public Inventory(String sku, Integer quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    //AFN_2_MFN
    public Inventory(String sku, Integer quantity, String fulfillmentLatency, String switchFulfillmentTo) {
        this.sku = sku;
        this.quantity = quantity;
        this.fulfillmentLatency = fulfillmentLatency;
        this.switchFulfillmentTo = switchFulfillmentTo;
    }

    public Inventory(String sku, String fulfillmentCenterID, String lookup, String switchFulfillmentTo) {
        this.sku = sku;
        this.fulfillmentCenterID = fulfillmentCenterID;
        this.lookup = lookup;
        this.switchFulfillmentTo = switchFulfillmentTo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getFulfillmentCenterID() {
        return fulfillmentCenterID;
    }

    public void setFulfillmentCenterID(String fulfillmentCenterID) {
        this.fulfillmentCenterID = fulfillmentCenterID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getFulfillmentLatency() {
        return fulfillmentLatency;
    }

    public void setFulfillmentLatency(String fulfillmentLatency) {
        this.fulfillmentLatency = fulfillmentLatency;
    }

    public String getLookup() {
        return lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public String getSwitchFulfillmentTo() {
        return switchFulfillmentTo;
    }

    public void setSwitchFulfillmentTo(String switchFulfillmentTo) {
        this.switchFulfillmentTo = switchFulfillmentTo;
    }
}
