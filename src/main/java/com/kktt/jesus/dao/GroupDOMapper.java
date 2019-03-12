package com.kktt.jesus.dao;

import com.kktt.jesus.dataobject.GroupDO;

public interface GroupDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupDO record);

    int insertSelective(GroupDO record);

    GroupDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupDO record);

    int updateByPrimaryKey(GroupDO record);
}