package com.kktt.jesus;

import com.kktt.jesus.api.GoTenProductHandler;
import com.kktt.jesus.api.ProductConverter;
import com.kktt.jesus.dataobject.GotenProductEntity;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test extends BaseTest{

    @Resource
    private GoTenProductHandler goTenProductHandler;
    @Resource
    private ProductConverter productConverter;
    @org.junit.Test
    public void test(){
        List<GotenProductEntity> xx = productConverter.getProduct();
        System.out.println(1);
    }
}
