package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.PromoDOMapper;
import com.miaoshaproject.dataobject.PromoDO;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @描述
 * @创建人 wpfei
 * @创建时间 2019-08-16
 */
@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ItemService itemService;

    @Override
    public void publishPromo(Long promoId) {
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        if (null == promoDO || promoDO.getItemId().longValue() == 0){
            return ;
        }
        ItemModel itemModel = itemService.getItemById(promoDO.getItemId());
        //将库存同步到redis上
        redisTemplate.opsForValue().set("promo_item_stock_"+itemModel.getId(),itemModel.getStock());
        redisTemplate.expire("promo_item_stock_"+itemModel.getId(),30, TimeUnit.MINUTES);
    }

    @Override
    public PromoModel getPromoByItemId(Long itemId) {
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);
        PromoModel promoModel = convertModelFromDataObject(promoDO);
        //判断当前时间是否是秒杀活动即将开始或者正在进行
        if (null == promoModel) {
            return null;
        }
        if (promoModel.getStartDate().isAfterNow()) {
            promoModel.setStatus(1);
        } else if (promoModel.getStartDate().isBeforeNow()) {
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

        return promoModel;
    }



    private PromoDO convertFromPromoModel(PromoModel promoModel) {
        PromoDO promoDO = new PromoDO();
        BeanUtils.copyProperties(promoModel, promoDO);

        return promoDO;

    }

    private PromoModel convertModelFromDataObject(PromoDO promoDO) {
        if (null == promoDO) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;

    }
}
