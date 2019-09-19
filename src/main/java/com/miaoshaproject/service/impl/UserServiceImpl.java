package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationImpl;
import com.miaoshaproject.validator.ValidationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidationImpl validator;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ItemService itemService;
    @Override

    @Async
    public void test() throws InterruptedException {
        Thread.sleep(1000*30);
        System.out.println("我已经休息了30秒");
    }
    @Override
    public UserModel getUserByIdInCache(Long id) {
        UserModel userModel = (UserModel)redisTemplate.opsForValue().get("user_validate_" + id);
        if (null == userModel){
            userModel = this.getUserById(id);
            redisTemplate.opsForValue().set("user_validate_" + id,userModel);
            redisTemplate.expire("user_validate_" + id,10, TimeUnit.MINUTES);
        }
        return userModel;
    }



    @Override
    public UserModel getUserById(Long id) {
        //调用userDomapper获取对应用户id的dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        UserModel userModel = new UserModel();
        if (null != userDO) {
            UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
            userModel = convertFromDataObject(userDO, userPasswordDO);
        }
        return userModel;

    }

    /**
     * @描述：用户注册方法
     * @参数 [userModel]
     * @返回值 void
     * @创建人 wpfei
     * @创建时间 2019-08-13
     * @修改人和其它信息
     **/

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (null == userModel) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if (StringUtils.isEmpty(userModel.getName())
//                || null == userModel.getGender()
//                || null == userModel.getAge()
//                || StringUtils.isEmpty(userModel.getTelphone())
//                ) {
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult result = validator.validate(userModel);
        if (result.isHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        //使用model->dataobject的方法
        UserDO userDo = convertFromModel(userModel);
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDo);
        }catch (DuplicateKeyException e)
        {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号码重复注册1");
        }
        userPasswordDO.setUserId(userDo.getId());
        userPasswordDOMapper.insertSelective(userPasswordDO);
        return;

    }

    @Override
    public UserModel validateLogin(String telphone, String password) throws BusinessException {
        //通过用户的手机获取用户信息
       UserDO userDO = userDOMapper.selectByTelphone(telphone);
       if (null == userDO){
           throw  new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
       }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
       UserModel userModel = convertFromDataObject(userDO,userPasswordDO);
        //对比用户密码和传进来（加密后）的密码是否匹配
       if (!StringUtils.equals(password,userPasswordDO.getEncrptPassword())){
           throw  new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
       }

        return userModel;
    }



    private UserPasswordDO convertPasswordFromModel(UserModel userModel) {
        if (null == userModel) {
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setUserId(userModel.getId());
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        return userPasswordDO;

    }
    private UserDO convertFromModel(UserModel userModel) {
        if (null == userModel) {
            return null;
        }
        UserDO userDo = new UserDO();
        BeanUtils.copyProperties(userModel, userDo);
        return userDo;

    }


    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (null == userDO) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        if (null != userPasswordDO) {
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }

        return userModel;
    }
}
