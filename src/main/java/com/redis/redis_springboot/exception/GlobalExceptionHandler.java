package com.redis.redis_springboot.exception;


import com.redis.redis_springboot.util.FrontResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages={"com.redis.redis_springboot.controller"})
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public FrontResult handleException(Exception e){
        System.out.println("Exception处理:" + e);
       return FrontResult.getExceptionResult(null,"");

    }



}
