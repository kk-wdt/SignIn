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
    "standardPrice"
})
@XmlRootElement(name = "Price")
public class Price {

    @XmlElement(name = "SKU", required = true)
    protected String sku;
    @XmlElement(name = "StandardPrice", required = true)
    protected StandardPrice standardPrice;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public StandardPrice getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(StandardPrice standardPrice) {
        this.standardPrice = standardPrice;
    }


    public Price(){}

    public Price(String sku, StandardPrice standardPrice) {
        this.sku = sku;
        this.standardPrice = standardPrice;
    }
}
