package com.redis.redis_springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redis.redis_springboot.bean.YcfVpn;
import org.springframework.stereotype.Repository;

@Repository
public interface YcfVpnDao extends BaseMapper<YcfVpn> {

    Integer listCount() throws Exception;
}
