package com.miaoshaproject.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @描述
 * @创建人 wpfei
 * @创建时间 2019-08-13
 */

public class ValidationResult {
    //检查结果是否有错
    private boolean hasError;
    //存放错误信息
    private Map<String,String> errorMsgMap = new HashMap<>();

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }
    //实现通用的通过格式化字符串信息获取错误结果msg的方法
    public String getErrMsg(){
       return StringUtils.join(errorMsgMap.values().toArray(),",");
    }
}
