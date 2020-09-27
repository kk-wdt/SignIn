package com.kktt.jesus.service.impl;

import com.kktt.jesus.dao.GroupDOMapper;
import com.kktt.jesus.dataobject.GroupDO;
import com.kktt.jesus.errror.BusinessException;
import com.kktt.jesus.errror.EmBusinessError;
import com.kktt.jesus.service.GroupService;
import com.kktt.jesus.service.model.GroupModel;
import com.kktt.jesus.validator.ValidationResult;
import com.kktt.jesus.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    public GroupDOMapper groupDOMapper;
    @Autowired
    public ValidatorImpl validator;

    private GroupModel convertGroupModelFromDataObject(GroupDO groupDO){
        if (groupDO == null) {
            return null;
        }
        GroupModel groupModel = new GroupModel();
        BeanUtils.copyProperties(groupDO,groupModel);
        return groupModel;
    }

    private GroupDO convertGroupDOFromGroupModel(GroupModel groupModel){
        if(groupModel == null){
            return null;
        }
        GroupDO groupDO = new GroupDO();
        BeanUtils.copyProperties(groupModel,groupDO);
        return  groupDO;
    }


    @Override
    public GroupModel add(GroupModel groupModel) throws BusinessException {
        //校验参数
        ValidationResult validationResult = validator.validator(groupModel);
        if(validationResult.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrorMsg());
        }
        GroupDO groupDO = convertGroupDOFromGroupModel(groupModel);
        groupDOMapper.insert(groupDO);
        return this.getGroupById(groupDO.getId());
    }

    @Override
    public GroupModel getGroupById(Integer id) {
        GroupDO groupDO = groupDOMapper.selectByPrimaryKey(id);
        if(groupDO == null){
            return null;
        }
        return convertGroupModelFromDataObject(groupDO);
    }


}