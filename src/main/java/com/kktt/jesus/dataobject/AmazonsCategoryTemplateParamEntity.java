package com.kktt.jesus.dataobject;

public class AmazonsCategoryTemplateParamEntity {

    private Integer id;

    private String nodeId;

    private String fieldName;

    private String labelName;

    private String acceptedValue;

    private String example;

    private String groupName;

    private String values;

    private String type;

    private Integer maxOccur;

    private Byte isRequire;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getAcceptedValue() {
        return acceptedValue;
    }

    public void setAcceptedValue(String acceptedValue) {
        this.acceptedValue = acceptedValue;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Byte getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(Byte isRequire) {
        this.isRequire = isRequire;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getMaxOccur() {
        return maxOccur;
    }

    public void setMaxOccur(Integer maxOccur) {
        this.maxOccur = maxOccur;
    }
}
