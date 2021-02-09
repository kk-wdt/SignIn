package com.kktt.jesus.dataobject;

import lombok.Data;

import java.util.List;

@Data
public class AmazonErrorInfo {

    private List<String> message;

    private String errorCode;

}
