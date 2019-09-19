package com.miaoshaproject.controller;

public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Object handleExcption(HttpServletRequest request, Exception ex){
//        Map<String,Object> resopnseData = new HashMap<>();
//        if(ex instanceof BusinessException){
//            BusinessException businessException = (BusinessException)ex;
//            resopnseData.put("errCode",businessException.getErrCode());
//            resopnseData.put("errMsg",businessException.getErrMsg());
//        }else{
//            resopnseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
//            resopnseData.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
//        }
//
//        return CommonReturnType.create(resopnseData,"fail");
//    }
}
