package com.kktt.jesus.dataobject;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Table;

@Table(name = "common")
public class CommonEntity {
    public interface TYPE{
        Integer AD = 1;
        Integer BUSINESS = 2;
        Integer OPERATION = 3;
        Integer REVIEW = 4;
    }

    @javax.persistence.Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String uri;

    private String method;

    private Integer isWriteRequest;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getIsWriteRequest() {
        return isWriteRequest;
    }

    public void setIsWriteRequest(Integer isWriteRequest) {
        this.isWriteRequest = isWriteRequest;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
