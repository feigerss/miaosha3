package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Long userId,Long itemId,Long promoId, Integer amount) throws BusinessException;
}
