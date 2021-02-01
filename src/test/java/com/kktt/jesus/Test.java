package com.kktt.jesus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kktt.jesus.api.GoTenProductHandler;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test extends BaseTest{

    @Resource
    private GoTenProductHandler goTenProductHandler;

    @org.junit.Test
    public void test(){
        goTenProductHandler.getProduct();


    }
}
