package com.miaoshaproject.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @描述
 * @创建人 wpfei
 * @创建时间 2019-08-13
 */

public class ItemModel implements Serializable{
    private Long id;

    //商品名称
    @NotBlank(message = "商品名称不能为空！")
    private String title;
    //商品价格
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0,message = "商品价格必须大于0")
    private BigDecimal price;
    //商品库存
    @NotNull(message = "库存不能为空！")
    private Integer stock;
    //商品描述
    @NotBlank(message = "商品描述不能为空！")
    private String description;
    //商品销量
    private Integer sales;
    //商品图片地址
    @NotBlank(message = "商品图片不能为空！")
    private String imgUrl;
    //如果PromoModel不为空，则表示有还未结束的秒杀活动
    private PromoModel promoModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }
}
