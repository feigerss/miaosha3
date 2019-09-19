package com.miaoshaproject.controller.viewobject;

import java.math.BigDecimal;

/**
 * @描述
 * @创建人 wpfei
 * @创建时间 2019-08-16
 */

public class ItemVo {
    private Long id;
    //商品名称
    private String title;
    //商品价格
    private BigDecimal price;
    //商品库存
    private Integer stock;
    //商品描述
    private String description;
    //商品销量
    private Integer sales;
    //商品图片地址
    private String imgUrl;
    //记录商品是否在秒杀活动中,及对应的状态，0表示没有秒杀活动，1表示秒杀活动代开始，2表示活动进行中
    private Integer ptomoStatus;
    //秒杀活动价格
    private BigDecimal promoPrice;
    //秒杀活动id
    private Long promoId;
    //秒杀活动开始时间
    private String startDate;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPtomoStatus() {
        return ptomoStatus;
    }

    public void setPtomoStatus(Integer ptomoStatus) {
        this.ptomoStatus = ptomoStatus;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Long getPromoId() {
        return promoId;
    }

    public void setPromoId(Long promoId) {
        this.promoId = promoId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
