package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.ItemVo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.CacheService;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @描述
 * @创建人 wpfei
 * @创建时间 2019-08-16
 */
@RestController("/item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", origins = {"*"})
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private PromoService promoService;

    @RequestMapping(value = "/publishpromo", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType publishpromo(@RequestParam(name = "id") Long id) {
        promoService.publishPromo(id);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
        //封装service请求用来处理商品
        ItemModel itemModel = new ItemModel();
        itemModel.setStock(stock);
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setImgUrl(imgUrl);
        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        return CommonReturnType.create(convertVoFromModel(itemModelForReturn));
    }

    private ItemVo convertVoFromModel(ItemModel itemModel) {
        if (null == itemModel) {
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel, itemVo);
        if (null != itemModel.getPromoModel()) {
            //有即将开始或者正在进行中的秒杀活动
            itemVo.setPtomoStatus(itemModel.getPromoModel().getStatus());
            itemVo.setPromoId(itemModel.getPromoModel().getId());
            itemVo.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
            itemVo.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            itemVo.setPtomoStatus(0);
        }
        return itemVo;
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") Long id) {
        ItemModel itemModel = null;
        //先查询本地缓存，如果本地缓存中存在该数据，直接返回
        itemModel = (ItemModel) cacheService.getFromCommonCache("item_" + id);
        if (null == itemModel){
            System.out.println("本地缓存中无对应数据，查找redis");
            //然后在redis中查询，缓存中有没有该商品的详情数据
            itemModel = (ItemModel)redisTemplate.opsForValue().get("item_" + id);
            if(null == itemModel){
                System.out.println("redis缓存中无对应数据，查找数据库");
                //如果缓存中没有该商品的详情数据，则调用下游service，并将数据缓存到redis中
                itemModel = itemService.getItemById(id);
                redisTemplate.opsForValue().set("item_" + id,itemModel);
                redisTemplate.expire("item_" + id,10, TimeUnit.MINUTES);
                System.out.println("将数据写入redis");
            }
            //填充本地缓存
            cacheService.setCommonCache("item_" + id,itemModel);
            System.out.println("将数据写入本地缓存");
        }

        return CommonReturnType.create(this.convertVoFromModel(itemModel));
    }

    /**
     * @描述：商品列表页面浏览
     * @参数 []
     * @返回值 com.miaoshaproject.response.CommonReturnType
     * @创建人 wpfei
     * @创建时间 2019-08-16
     * @修改人和其它信息
     **/

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem() {
        //查询商品列表
        List<ItemModel> itemModelList = itemService.listItem();
        //使用java8stream的方式将itemModel（List）转换为itemVo（List）
        List<ItemVo> itemVoList = itemModelList.stream().map(itemModel -> {
            return this.convertVoFromModel(itemModel);
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVoList);
    }
}
