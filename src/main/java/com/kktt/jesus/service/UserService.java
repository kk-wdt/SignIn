package com.kktt.jesus.service;

import com.kktt.jesus.pojo.User;

public interface UserService {
    public User getUserById(int userId);

    boolean addUser(User record);

}
