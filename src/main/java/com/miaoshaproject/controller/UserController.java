package com.miaoshaproject.controller;


import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.controller.viewobject.UserVo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",origins ={"*"} )
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType test() throws InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        userService.test();
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println( "task任务总耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
        return CommonReturnType.create();
    }

    //用户注册接口
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone") String telphone,@RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if (org.apache.commons.lang3.StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登录服务，用来校验用户登录是否合法
        UserModel userModel =  userService.validateLogin(telphone,this.EncodeByMd5(password));
        //将登录凭证加入到用户登录成功的session内
        //若用户验证登录成功后将对应的登录信息和凭证信息存储到redis中
        //生成登录凭证，UUID
        String uuidToken = UUID.randomUUID().toString().replace("-","");
        //建立token和用户之间的联系
        redisTemplate.opsForValue().set(uuidToken,userModel);
        redisTemplate.expire(uuidToken,1, TimeUnit.HOURS);
//        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
//        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        //下发token
        return CommonReturnType.create(uuidToken);
    }

    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getotp(@RequestParam(name = "telphone") String telphone) {
        //按照一定的规则生成otp验证码
        //将otp验证码同对应用户的手机号关联，使用httpsession方式绑定他的手机号与otp
        //将otp验证码通过短信通道发送给用户
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);
        httpServletRequest.getSession().setAttribute(telphone, otpCode);
        System.out.println("telphone：" + telphone + "&otpCode" + otpCode);
        return CommonReturnType.create();

    }
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Long id) throws BusinessException {
        //调用service 服务获取对应id的用户对象并且返回给前端
        UserModel userModel = userService.getUserById(id);
        //若获取的对应用户信息不存在
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //将核心领域模型用户对象转化为可供UI使用的viewobject
        UserVo userVo = convertFromModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVo);
    }

    private UserVo convertFromModel(UserModel userModel) {
        if (null == userModel) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel, userVo);
        return userVo;

    }

    //用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Byte gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password
    ) throws BusinessException,Exception{
        //验证手机号和otp是否对应
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        if(!StringUtils.equals(otpCode,inSessionOtpCode)){
            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不正确!");
        }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byPhone");
        userModel.setAge(age);
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setEncrptPassword(this.EncodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create();

    }
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException,UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

}
