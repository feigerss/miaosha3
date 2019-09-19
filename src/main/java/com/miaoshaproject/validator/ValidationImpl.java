package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

/**
 * @描述
 * @创建人 wpfei
 * @创建时间 2019-08-13
 */
@Component
public class ValidationImpl implements InitializingBean{
    private javax.validation.Validator validator;
    //实现校验方法，并返回校验结果
public ValidationResult validate(Object bean){
    ValidationResult result = new ValidationResult();
    Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
    if (constraintViolationSet.size()>0){
        //有错误
        result.setHasError(true);
        constraintViolationSet.forEach(constraintViolation ->{
                String errorMsg=constraintViolation.getMessage();
                    String properyName=constraintViolation.getPropertyPath().toString();
                    result.getErrorMsgMap().put(properyName,errorMsg);
        });

    }
    return result;
}
    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator通过工厂的初始化方法使其实例化
        this.validator= Validation.buildDefaultValidatorFactory().getValidator();
    }
}
