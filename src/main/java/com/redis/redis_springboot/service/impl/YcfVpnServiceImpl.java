package com.redis.redis_springboot.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.redis_springboot.bean.YcfVpn;
import com.redis.redis_springboot.dao.YcfVpnDao;
import com.redis.redis_springboot.service.YcfVpnService;
import com.redis.redis_springboot.util.IpUtils;
import com.redis.redis_springboot.util.RedisUtil;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class YcfVpnServiceImpl extends ServiceImpl<YcfVpnDao, YcfVpn> implements YcfVpnService{

    @Autowired(required = false)
    private YcfVpnDao ycfVpnDao;

    @Autowired
    private RedisUtil redisUtil;

    @Synchronized
    @Override
    public Integer listCount() throws Exception{

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        System.out.println("在非controller中获取HttpServletRequest (如在service中获取)  =="+ IpUtils.getRequestIP(request));

        Integer count = ycfVpnDao.listCount();
        if(count!=null && count >=0){
            //只有一个对象可以用方法锁。
            for (int i = 0; i < 10; i++) {
                try {//sleep会发生异常要显示处理
                    Thread.sleep(20);//暂停20毫秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "打了:" + i + "个小兵");
            }
            return count;

        }
        return null;
    }
}
