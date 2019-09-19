package com.miaoshaproject.response;

public class CommonReturnType {
    //表明请求或者失败
    private String status;
    private Object data;

    //定义通用的创建方法
    public static CommonReturnType create( ){
        return CommonReturnType.create(null , "success");
    }

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result , "success");
    }
    public static CommonReturnType create(Object result,String status){
        CommonReturnType type= new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
