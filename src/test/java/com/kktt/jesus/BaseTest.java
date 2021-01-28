package com.kktt.jesus;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseTest {
    public static final String DOMIN = "https://api.gotenchina.com/";
    public static final String token = "yXmV1Xf1GFXGjTK8z2x4Bw==";


    protected String encrypt(String source){
        return encrypt(source,"z2o7Dn8w");
    }

    protected String encrypt(String source,String key){
        try {
            //创建密钥规则
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            byte[] keyBytes = key.getBytes();

            //创建密钥工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //按照密钥规则生成密钥
            SecretKey secretKey =  keyFactory.generateSecret(keySpec);
            //加密对象
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            //初始化加密对象需要的属性
            AlgorithmParameterSpec iv = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            //开始加密
            byte[] result = cipher.doFinal(source.getBytes());
            //Base64加密
            return  new BASE64Encoder().encode(result) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }

    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

}
