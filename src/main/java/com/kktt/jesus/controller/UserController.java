package com.kktt.jesus.controller;

import com.kktt.jesus.errror.BusinessException;
import com.kktt.jesus.errror.EmBusinessError;
import com.kktt.jesus.response.CommonReturnType;
import com.kktt.jesus.service.UserService;
import com.kktt.jesus.service.model.UserModel;
import com.sun.tools.javac.util.Assert;
import org.hibernate.annotations.Parameter;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;

    @RequestMapping("/showUser")
    @ResponseBody
    public CommonReturnType toIndex(Integer id) throws BusinessException {
        if(id == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserModel user = userService.getUserById(id);
        if (user == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        return CommonReturnType.create(user);
    }
}

