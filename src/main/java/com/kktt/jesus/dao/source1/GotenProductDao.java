package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.GotenProduct;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface GotenProductDao  extends Mapper<GotenProduct> {

    int insertIgnore(@Param("entity") GotenProduct record);

}