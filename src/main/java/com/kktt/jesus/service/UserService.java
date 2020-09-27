package com.kktt.jesus.service;

import com.kktt.jesus.service.model.UserModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserService {
    public UserModel getUserById(int userId);

}
