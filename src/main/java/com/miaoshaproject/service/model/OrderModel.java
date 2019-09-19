package com.miaoshaproject.service.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @描述
 * @创建人 wpfei
 * @创建时间 2019-08-16
 */
//用户下单交易模型
public class OrderModel implements Serializable {
    //2019081688888888
    //交易号
    private String id;
    //顾客身份id
    private Long userId;
    //购买的商品id
    private Long itemId;
    //秒杀id，若非空，则表示该商品是以秒杀方式下单
    private Long promoId;
    //购买的数量
    private Integer amount;
    //购买金额，若promoId非空，则表示该商品秒杀金额
    private BigDecimal orderPrice;
    //购买商品的单价，若promoId非空，则表示该商品秒杀单价
    private BigDecimal itemPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Long getPromoId() {
        return promoId;
    }

    public void setPromoId(Long promoId) {
        this.promoId = promoId;
    }
}
