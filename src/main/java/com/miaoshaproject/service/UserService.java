package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

public interface UserService {
    //通过用户id获取用户对象的方法
    UserModel getUserById(Long id);
    void register(UserModel userModel) throws BusinessException;
    UserModel validateLogin(String telphone, String password) throws BusinessException;
    //通过缓存获取用户对象的方法
    UserModel getUserByIdInCache(Long id);

    void test() throws InterruptedException;
}
