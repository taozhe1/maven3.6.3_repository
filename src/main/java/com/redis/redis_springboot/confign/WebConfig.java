package com.redis.redis_springboot.confign;

import com.redis.redis_springboot.interceptor.IpUrlLimitInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration和@Bean组合使用的目的与@Service相同，均可用来创建Bean。其中，@Configuration和@Bean注解主要是用在配置文件类中，@Service主要是用在实体类中
//总之，@Bean注解用于标记一个方法，该方法将返回一个Bean对象，并由Spring容器进行管理。这使得我们可以通过@Autowired注解在其他类中使用该Bean对象。
//还有一种方法，有个一个注解@component：  标注一个类为Spring容器的Bean

//自定义拦截器
@Configuration
public class WebConfig implements WebMvcConfigurer {


    //把你写的自定义拦截器实例注入进来，并将自定义拦截器传入;
    @Bean
    IpUrlLimitInterceptor getIpUrlLimitInterceptor(){
        return  new IpUrlLimitInterceptor();
    };


    /**
     * 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
     *
     * .addPathPatterns("/user/**")  // 拦截以 /user 开头的请求
     * .excludePathPatterns("/api/login"); // 排除 /api/login 请求不拦截
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //创建addPathPatterns方法（指定拦截路径，往往使用 "/**"）
        registry.addInterceptor(getIpUrlLimitInterceptor()).addPathPatterns("/devRedis/addItemNotExists/**");

    }

}
