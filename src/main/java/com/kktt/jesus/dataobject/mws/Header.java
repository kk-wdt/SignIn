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
import java.math.BigDecimal;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "documentVersion",
    "merchantIdentifier"
})
@XmlRootElement(name = "Header")
public class Header {

    @XmlElement(name = "DocumentVersion", required = true)
    protected BigDecimal documentVersion;
    @XmlElement(name = "MerchantIdentifier", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String merchantIdentifier;

    /**
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDocumentVersion() {
        return documentVersion;
    }

    /**
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDocumentVersion(BigDecimal value) {
        this.documentVersion = value;
    }

    /**
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerchantIdentifier() {
        return merchantIdentifier;
    }

    /**
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerchantIdentifier(String value) {
        this.merchantIdentifier = value;
    }

}
