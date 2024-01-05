package com.redis.redis_springboot.util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * 安全服务工具类
 */

@Component
public class SecurityUtils {


/**
     * SecurityUtils.encodePassword("密码")
     * 生成BCryptPasswordEncoder密码
     * @param password 密码
     * @return 加密字符串
     */

    public  String encodePassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

/**
     * SecurityUtils.matchesPassword("用户输入的密码","数据库中存储的密码")
     * 判断密码是否相同
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */

    public  boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

