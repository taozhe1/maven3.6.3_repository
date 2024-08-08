package com.redis.redis_springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redis.redis_springboot.bean.TdGoods;

import java.util.List;


public interface TdGoodsService extends IService<TdGoods> {
    void orderById(TdGoods tdGoods) throws Exception;

    List<TdGoods> getByName(TdGoods tdGoods) throws Exception;

    List<TdGoods> getById(List<Long> id) throws Exception;
}
