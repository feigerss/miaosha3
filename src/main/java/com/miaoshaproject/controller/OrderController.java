package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @描述
 * @创建人 wpfei
 * @创建时间 2019-08-16$
 */
@RestController("/order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",origins ={"*"} )
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private RedisTemplate redisTemplate;
    //用户注册接口
    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Long itemId,
                                        @RequestParam(name = "promoId",required = false) Long promoId,
                                        @RequestParam(name = "amount") Integer amount) throws BusinessException{
        //获取用户登录信息
        String token = httpServletRequest.getParameterMap().get("token")[0];
        if(StringUtils.isEmpty(token)){
            throw  new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户未登录，不能下单!");
        }
        UserModel userModel = (UserModel)redisTemplate.opsForValue().get("token");
        if (null == userModel){
            throw  new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户未登录，不能下单!");
        }
        orderService.createOrder(userModel.getId(),itemId,promoId,amount);
        return CommonReturnType.create();
    }
}
