package com.kktt.jesus;

import com.kktt.jesus.api.ProductConverter;
import com.kktt.jesus.dao.source1.GotenProductDao;
import com.kktt.jesus.dataobject.CommonEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test extends BaseTest{

    @Resource
    private GotenProductDao gotenProductDao;
    @Resource
    private ProductConverter productConverter;
    @org.junit.Test
    public void test(){
        int index = 1;
        List<GotenProduct> xx = productConverter.getProduct(index,"2021-01-01","2021-02-03");
        while (!CollectionUtils.isEmpty(xx)){
            System.out.println("数据长度:"+xx.size());
            saveProduct(xx);
            index ++;
            xx = productConverter.getProduct(index,"2021-01-01","2021-02-03");
        }
    }

    @org.junit.Test
    public void getSingleProduct(){
        productConverter.getProduct(44075501L);
    }

    private void saveProduct(List<GotenProduct> xx ){
        for (GotenProduct product : xx) {
            GotenProduct exist = find(product.getSku());
            if(exist == null){
                gotenProductDao.insertSelective(product);
            }
        }
    }

    @org.junit.Test
    public void test1(){
        Example example = new Example(GotenProduct.class);
        example.createCriteria().andEqualTo("sku", "85169910");
        GotenProduct xx = gotenProductDao.selectOneByExample(example);
        System.out.println(1);
    }

    private GotenProduct find(Long sku){
        Example example = new Example(GotenProduct.class);
        example.createCriteria().andEqualTo("sku", sku);
        GotenProduct xx = gotenProductDao.selectOneByExample(example);
        return xx;
    }
}
