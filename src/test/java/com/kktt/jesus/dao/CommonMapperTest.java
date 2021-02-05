package com.kktt.jesus.dao;

import com.kktt.jesus.dao.source1.CommonMapper;
import com.kktt.jesus.dao.source1.GotenProductDao;
import com.kktt.jesus.dao.source2.Common2Mapper;
import com.kktt.jesus.dataobject.CommonEntity;
import com.kktt.jesus.dataobject.GotenProduct;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonMapperTest {

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private Common2Mapper common2Mapper;
    @Test
    public void findFromDB1(){
        CommonEntity entity = commonMapper.selectByPrimaryKey(1);
        Assert.assertEquals(1, (int) entity.getId());
    }

    @Test
    public void findFromDB2(){
        CommonEntity entity = common2Mapper.selectByPrimaryKey(2);
        Assert.assertEquals(2, (int) entity.getId());
    }

    @Test
    public void insert(){
        CommonEntity commonEntity = new CommonEntity();
        commonEntity.setIsWriteRequest(1);
        commonEntity.setMethod("me");
        commonEntity.setType(2);
        commonEntity.setUri("www.te.com");
        int count = commonMapper.insert(commonEntity);
        Assert.assertEquals(1,count);
    }

    @Test
    public void example(){
        Example example = new Example(CommonEntity.class);
        example.createCriteria().andEqualTo("type", 2);
        List<CommonEntity> xx = commonMapper.selectByExample(example);
        System.out.println(1);
    }

    @Resource
    private GotenProductDao gotenProductDao;
    @Test
    public void test(){
        List<GotenProduct> xx = gotenProductDao.selectAll();
        System.out.println(1);
    }
}
