package com.kktt.jesus.utils;

import java.io.File;
import java.util.UUID;

public class FileUtil {

    public static void deleteFile(String filePath){
        File file =  new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }

    public static String getFilePath(){
        String uuid = UUID.randomUUID().toString();
        String os = System.getProperty("os.name");
        String filePath;
        if(os.toLowerCase().startsWith("win")){
            filePath="C:\\tmp\\"+uuid+".xml";
        }else{
            filePath = "/tmp/"+uuid+".xml";
        }
        return filePath;
    }

    public static String getTempImagePath(){
        String uuid = UUID.randomUUID().toString();
        String os = System.getProperty("os.name");
        String filePath;
        if(os.toLowerCase().startsWith("win")){
            filePath="C:\\tmp\\"+uuid+".jpg";
        }else{
            filePath = "/tmp/"+uuid+".jpg";
        }
        return filePath;
    }

    public static String getTmpPath(String suffix){
        String uuid = UUID.randomUUID().toString();
        String os = System.getProperty("os.name");
        String filePath;
        if(os.toLowerCase().startsWith("win")){
            filePath="C:\\tmp\\"+uuid+"."+suffix;
        }else{
            filePath = "/tmp/"+uuid+"."+suffix;
        }
        return filePath;
    }
}
