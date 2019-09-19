package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.ItemDOMapper;
import com.miaoshaproject.dao.ItemStockDOMapper;
import com.miaoshaproject.dataobject.ItemDO;
import com.miaoshaproject.dataobject.ItemStockDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import com.miaoshaproject.validator.ValidationImpl;
import com.miaoshaproject.validator.ValidationResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @描述
 * @创建人 wpfei
 * @创建时间 2019-08-14 16:14:00
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidationImpl validator;
    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;
    @Autowired
    private PromoService promoService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;


    @Override
    public ItemModel getItemByIdInCache(Long id) {
        ItemModel itemModel = (ItemModel)redisTemplate.opsForValue().get("item_validate_" + id);
        if (null == itemModel){
            itemModel = this.getItemById(id);
            redisTemplate.opsForValue().set("item_validate_" + id,itemModel);
          //  redisTemplate.expire("item_validate_" + id,10, TimeUnit.MINUTES);
        }
        return itemModel;
    }


    @Override
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //入参校验
//        if (null == itemModel) {
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasError()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        //转换ItemModel->ItemDO
        ItemDO itemDO = this.convertFromModel(itemModel);
        //写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        //返回创建完成的对象


        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.listItem();
        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            return this.convertModelFromDataObject(itemDO, itemStockDO);
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Long id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (null == itemDO) {
            return null;
        }
        //获取库存信息
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        //将dataobject转换为model
        ItemModel itemModel = this.convertModelFromDataObject(itemDO, itemStockDO);
        //获取活动信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if (null != promoModel && promoModel.getStatus().intValue() != 3) {
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Long itemId, Integer amount) throws BusinessException {
       long result= redisTemplate.opsForValue().increment("item_validate_" + itemId,amount.intValue()* -1);
        //int affectedRow = itemStockDOMapper.decreaseStock(itemId, amount);
        if (result >= 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void increaseSales(Long itemId, Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId, amount);
        return;
    }



    private ItemDO convertFromModel(ItemModel itemModel) {
        if (null == itemModel) {
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;

    }

    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel) {
        if (null == itemModel) {
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());

        return itemStockDO;

    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
