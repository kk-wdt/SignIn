package com.kktt.jesus.dao;

import com.kktt.jesus.dao.source1.CommonMapper;
import com.kktt.jesus.dao.source2.Common2Mapper;
import com.kktt.jesus.dataobject.CommonEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonMapperTest {

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private Common2Mapper common2Mapper;
    @Test
    public void find(){
        CommonEntity entity = commonMapper.selectByPrimaryKey(1);
        Assert.assertEquals(1, (int) entity.getId());
    }

    @Test
    public void find2(){
        CommonEntity entity = common2Mapper.selectByPrimaryKey(2);
        Assert.assertEquals(2, (int) entity.getId());
    }

}
