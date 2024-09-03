package com.redis.redis_springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redis.redis_springboot.bean.TdGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TdGoodsDao extends BaseMapper<TdGoods> {


    List<TdGoods> getByName(TdGoods tdGoods);

    int orderById(TdGoods tdGoods);

    List<TdGoods> getById(List<Long> id);
}
