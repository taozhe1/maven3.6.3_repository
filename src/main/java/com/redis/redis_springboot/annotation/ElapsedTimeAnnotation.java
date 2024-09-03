package com.redis.redis_springboot.annotation;
import java.lang.annotation.*;

/**
 * @author macbook
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ElapsedTimeAnnotation {


    // 默认拦截
    boolean needLog() default true;

    String arbitrarily() default "";
}
