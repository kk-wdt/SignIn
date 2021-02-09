//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2020.04.09 ʱ�� 01:59:11 PM CST 
//


package com.kktt.jesus.dataobject.mws;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "header",
        "messageType",
        "messages"
})
@XmlRootElement(name = "AmazonEnvelope")
public class PriceAmazonEnvelope {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "MessageType", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String messageType;
    @XmlElement(name = "Message", required = true)
    protected List<PriceMessage> messages;
    @XmlAttribute(name = "targetNamespace", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String targetNamespace;

    /**
     * 获取header属性的值。
     *
     * @return
     *     possible object is
     *     {@link Header }
     *
     */
    public Header getHeader() {
        return header;
    }

    /**
     * 设置header属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link Header }
     *
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    /**
     * 获取messageType属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * 设置messageType属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessageType(String value) {
        this.messageType = value;
    }


    /**
     * 获取targetNamespace属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTargetNamespace() {
        return targetNamespace;
    }

    /**
     * 设置targetNamespace属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTargetNamespace(String value) {
        this.targetNamespace = value;
    }

    public List<PriceMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<PriceMessage> messages) {
        this.messages = messages;
    }
}
