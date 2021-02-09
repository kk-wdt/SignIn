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
    "operationType",
    "inventory",

})
@XmlRootElement(name = "Message")
public class InventoryMessage {

    @XmlElement(name = "MessageID", required = true)
    protected BigInteger messageID;

    @XmlElement(name = "OperationType", required = true)
    protected String operationType;

    @XmlElement(name = "Inventory", required = false)
    protected Inventory inventory;

    public BigInteger getMessageID() {
        return messageID;
    }

    public void setMessageID(BigInteger messageID) {
        this.messageID = messageID;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
