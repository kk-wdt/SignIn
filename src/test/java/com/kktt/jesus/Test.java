package com.kktt.jesus;

import com.kktt.jesus.api.ProductConverter;
import com.kktt.jesus.dao.source1.GotenProductDao;
import com.kktt.jesus.dataobject.CommonEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import com.kktt.jesus.utils.CommonUtil;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
        List<GotenProduct> xx = productConverter.getProduct(index,"2020-12-01","2021-01-01");
        while (!CollectionUtils.isEmpty(xx)){
            System.out.println("数据长度:"+xx.size());
            saveProduct(xx);
            index ++;
            xx = productConverter.getProduct(index,"2020-12-01","2021-01-01");
        }
    }

    @org.junit.Test
    public void updatePrice(){
        List<GotenProduct> all = gotenProductDao.selectAll();
        if(CollectionUtils.isEmpty(all)){
            return;
        }
        List<Long> skuList = all.stream().map(GotenProduct::getSku).collect(Collectors.toList());
        List<List<Long>> skuGroup = CommonUtil.subCollection(skuList, 50);
        for (List<Long> group : skuGroup) {
            Map<Long, BigDecimal> priceMap = productConverter.getProductPrice(group);
            updateProductPrice(priceMap);
        }
    }

    @org.junit.Test
    public void updateQuantity(){
        List<GotenProduct> all = gotenProductDao.selectAll();
        if(CollectionUtils.isEmpty(all)){
            return;
        }
        List<Long> skuList = all.stream().map(GotenProduct::getSku).collect(Collectors.toList());
        List<List<Long>> skuGroup = CommonUtil.subCollection(skuList, 50);
        for (List<Long> group : skuGroup) {
            Map<Long, Integer> priceMap = productConverter.getProductInventory(group);
            updateProductInventory(priceMap);
        }
    }

    private void updateProductInventory(Map<Long,Integer> inventoryMap) {
        List<Map<String,Object>> datas = new ArrayList<>();
        inventoryMap.forEach((k,v)->{
            Map<String,Object> item = new HashMap<>();
            item.put("sku",k);
            item.put("inventory",v);
            datas.add(item);
        });
        gotenProductDao.batchUpdateInventory(datas);
    }

    private void updateProductPrice(Map<Long,BigDecimal> priceMap) {
        List<Map<String,Object>> datas = new ArrayList<>();
        priceMap.forEach((k,v)->{
            Map<String,Object> item = new HashMap<>();
            item.put("sku",k);
            item.put("price",v);
            datas.add(item);
        });
        gotenProductDao.batchUpdatePrice(datas);
    }

    @org.junit.Test
    public void getSingleProduct(){
        productConverter.getProduct(71549874L);
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

    private void saveProduct(List<GotenProduct> xx ){
        for (GotenProduct product : xx) {
//            GotenProduct exist = find(product.getSku());
//            if(exist == null){
//                gotenProductDao.insertSelective(product);
//            }
            gotenProductDao.insertIgnore(product);
        }
    }

}
