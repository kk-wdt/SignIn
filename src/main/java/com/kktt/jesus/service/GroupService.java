package com.kktt.jesus.service;

import com.kktt.jesus.errror.BusinessException;
import com.kktt.jesus.service.model.GroupModel;

public interface GroupService {
    public GroupModel add(GroupModel groupModel) throws BusinessException;

    public GroupModel getGroupById(Integer id);
}
