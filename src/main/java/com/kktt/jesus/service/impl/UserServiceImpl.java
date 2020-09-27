package com.kktt.jesus.service.impl;

import com.kktt.jesus.dao.UserDOMapper;
import com.kktt.jesus.dataobject.UserDO;
import com.kktt.jesus.service.UserService;
import com.kktt.jesus.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    public UserModel getUserById(int userId) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(userId);
        return convertFromDataObject(userDO);
    }


    private UserModel convertFromDataObject(UserDO userDO){
        if (userDO == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        return userModel;
    }


}