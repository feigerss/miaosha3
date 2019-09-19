package com.miaoshaproject.service.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @描述
 * @创建人 wpfei
 * @创建时间 2019-08-16
 */

public class PromoModel implements Serializable {
    //秒杀活动状态,1:未开始，2：进行中，3：已经结束
    private Integer status;
    private Long id;
    //秒杀活动名称
    private String promoName;
    //秒杀活动开始时间
    private DateTime startDate;
    //秒杀活动结束时间
    private DateTime endDate;
    //秒杀活动使用商品id
    private Long itemId;
    //秒杀活动商品价格
    private BigDecimal promoItemPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
