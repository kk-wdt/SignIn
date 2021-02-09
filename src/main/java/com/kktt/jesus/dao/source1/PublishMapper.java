package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.AliexpressSkuPublishEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface PublishMapper extends Mapper<AliexpressSkuPublishEntity> {
    void batchUpdate(@Param("list") List<AliexpressSkuPublishEntity> updateList);
}