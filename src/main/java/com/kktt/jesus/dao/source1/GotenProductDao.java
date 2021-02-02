package com.kktt.jesus.dao.source1;

import com.kktt.jesus.dataobject.GotenProductEntity;

public interface GotenProductDao {
    int deleteByPrimaryKey(String id);

    int insert(GotenProductEntity record);

    int insertSelective(GotenProductEntity record);

    GotenProductEntity selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(GotenProductEntity record);

    int updateByPrimaryKey(GotenProductEntity record);
}