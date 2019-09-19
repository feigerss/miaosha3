package com.miaoshaproject.service.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserModel implements Serializable{

    //用户id
    private Long id;
    //用户名
    @NotBlank(message = "用户名不能为空")
    private String name;
    //用户性别
    @NotNull(message = "性别必须填写")
    private Byte gender;
    //用户年龄
    @NotNull(message = "年龄必须填写")
    @Min(value = 0,message = "年龄必须大于0岁")
    @Max(value = 150,message = "年龄必须小于150岁")
    private Integer age;
    //手机号码
    @NotBlank(message = "手机号码不能为空")
    private String telphone;
    //注册方式
    private String registerMode;
    //第三方认证
    private String thridPartyId;
    //用户密码
    private String encrptPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public String getThridPartyId() {
        return thridPartyId;
    }

    public void setThridPartyId(String thridPartyId) {
        this.thridPartyId = thridPartyId;
    }

    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }
}
