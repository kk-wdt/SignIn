//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2020.04.09 ʱ�� 01:59:11 PM CST 
//


package com.kktt.jesus.dataobject.mws;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "messageID",
    "price"
})
@XmlRootElement(name = "Message")
public class PriceMessage {

    @XmlElement(name = "MessageID", required = true)
    protected BigInteger messageID;
    @XmlElement(name = "Price", required = false)
    protected Price price;

    /**
     *  获取messageID属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMessageID() {
        return messageID;
    }

    /**
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMessageID(BigInteger value) {
        this.messageID = value;
    }

    /**
     *
     * @return
     *     possible object is
     *     {@link Price }
     *
     */
    public Price getPrice() {
        return price;
    }

    /**
     *
     * @param value
     *     allowed object is
     *     {@link Price }
     *
     */
    public void setPrice(Price value) {
        this.price = value;
    }

}
