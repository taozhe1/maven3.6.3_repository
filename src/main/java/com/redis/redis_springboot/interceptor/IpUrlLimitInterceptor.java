package com.redis.redis_springboot.interceptor;

import com.redis.redis_springboot.util.IpUtils;
import com.redis.redis_springboot.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * https://www.cnblogs.com/alanlin/p/16267497.html
 * 拦截器的执行时间有三个（控制器方法之前、控制器方法之后、请求完成之后）。过滤器执行时间只在请求之前。  拦截器是对controller、动态资源进行拦截的。
 */
@Slf4j
public class IpUrlLimitInterceptor implements HandlerInterceptor {

    @Resource
    private RedisUtil redisUtils;
    private static  final  String LOCK_IP_URL_KEY="lock_ip_";

    private static  final String IP_URL_REQ_TIME="ip_url_times_";
    //访问次数限制
    private static final long LIMIT_TIMES=5;

    //限制时间 秒为单位
    private static final int IP_LOCK_TIME=60;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("request请求地址uri={},ip={}",request.getRequestURI(), IpUtils.getRequestIP(request));
        if(ipIsLock(IpUtils.getRequestIP(request))){
            log.info("ip访问被禁止={}",IpUtils.getRequestIP(request));
            throw  new Exception("当前操作过于频繁，请1分钟后重试");
        }
        if (!addRequestTime(IpUtils.getRequestIP(request),request.getRequestURI())){
            log.info("当前{}操作过于频繁，请1分钟后重试",IpUtils.getRequestIP(request));
            throw  new Exception("当前操作过于频繁，请1分钟后重试");
        }
        return true;
    }

    private boolean addRequestTime(String ip, String uri) {
        String key = IP_URL_REQ_TIME+ip+uri;
        if(redisUtils.hasKey(key)){
            long time=redisUtils.incr(key,(long)1);
            if(time >=LIMIT_TIMES){
                redisUtils.set(LOCK_IP_URL_KEY+ip,IP_LOCK_TIME,IP_LOCK_TIME);
                return false;
            }
        }else {
            boolean set = redisUtils.set(key, (long) 1, 1);
        }
        return  true;
    }

    private boolean ipIsLock(String ip) {
        if(redisUtils.hasKey(LOCK_IP_URL_KEY+ip)){
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
