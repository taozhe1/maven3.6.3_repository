package com.redis.redis_springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redis.redis_springboot.bean.YcfVpn;

public interface YcfVpnService extends IService<YcfVpn> {

    Integer listCount() throws Exception;
}
