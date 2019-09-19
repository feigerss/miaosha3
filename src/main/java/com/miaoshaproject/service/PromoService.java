package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

public interface PromoService {
    //根据itemId获取或者正在进行的秒杀活动信息
    PromoModel getPromoByItemId(Long itemId);
    void publishPromo(Long promoId);
}
