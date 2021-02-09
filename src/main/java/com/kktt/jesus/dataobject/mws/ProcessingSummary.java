//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2020.04.09 ʱ�� 03:00:00 PM CST
//


package com.kktt.jesus.dataobject.mws;

import java.math.BigInteger;
import java.util.List;

public class ProcessingSummary {

    private String submissionId;

    private BigInteger messagesProcessed;
    private BigInteger messagesSuccessful;
    private BigInteger messagesWithError;
    private BigInteger messagesWithWarning;

    private List<Result> results;


    public ProcessingSummary(){}

    public ProcessingSummary(BigInteger messagesProcessed, BigInteger messagesSuccessful, BigInteger messagesWithError, BigInteger messagesWithWarning) {
        this.messagesProcessed = messagesProcessed;
        this.messagesSuccessful = messagesSuccessful;
        this.messagesWithError = messagesWithError;
        this.messagesWithWarning = messagesWithWarning;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     * 获取messagesProcessed属性的值。
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getMessagesProcessed() {
        return messagesProcessed;
    }

    /**
     * 设置messagesProcessed属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setMessagesProcessed(BigInteger value) {
        this.messagesProcessed = value;
    }

    /**
     * 获取messagesSuccessful属性的值。
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getMessagesSuccessful() {
        return messagesSuccessful;
    }

    /**
     * 设置messagesSuccessful属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setMessagesSuccessful(BigInteger value) {
        this.messagesSuccessful = value;
    }

    /**
     * 获取messagesWithError属性的值。
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getMessagesWithError() {
        return messagesWithError;
    }

    /**
     * 设置messagesWithError属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setMessagesWithError(BigInteger value) {
        this.messagesWithError = value;
    }

    /**
     * 获取messagesWithWarning属性的值。
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getMessagesWithWarning() {
        return messagesWithWarning;
    }

    /**
     * 设置messagesWithWarning属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setMessagesWithWarning(BigInteger value) {
        this.messagesWithWarning = value;
    }

    @Override
    public String toString() {
        return "ProcessingSummary{" +
                "submissionId='" + submissionId + '\'' +
                ", messagesProcessed=" + messagesProcessed +
                ", messagesSuccessful=" + messagesSuccessful +
                ", messagesWithError=" + messagesWithError +
                ", messagesWithWarning=" + messagesWithWarning +
                ", results=" + results +
                '}';
    }
}

