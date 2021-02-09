package com.kktt.jesus.utils;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;

@Component
public class OssUtil {
    //这里的配置可以在properties或者yml中进行配置
    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucketName}")
    private String bucketName;

    @Value("${oss.endPoint}")
    private  String  endPoint ;

    //文件直接上传的方式，filename 为定义的文件名字
    public void upload(File obj, String fileName) {
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
            ossClient.putObject(bucketName, fileName, obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    //文件字节方式进行上传，filename 为定义的文件名字
    public void upload(byte[] array, String fileName) {
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
            ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(array));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
