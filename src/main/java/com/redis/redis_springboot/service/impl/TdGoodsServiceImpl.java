package com.redis.redis_springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.redis_springboot.bean.TdGoods;
import com.redis.redis_springboot.constant.RMQConstant;
import com.redis.redis_springboot.dao.TdGoodsDao;
import com.redis.redis_springboot.service.TdGoodsService;
import com.redis.redis_springboot.util.RocketMqHelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class TdGoodsServiceImpl extends ServiceImpl<TdGoodsDao, TdGoods> implements TdGoodsService {


    @Autowired
    private TdGoodsDao goodsDao;

    @Autowired
    private RocketMqHelperUtils rocketMqHelperUtils;


    @Override
    public void orderById(TdGoods tdGoods) throws Exception{
        log.info("ServiceImpl.orderById日志进来--->{}", JSON.toJSONString(tdGoods));
        rocketMqHelperUtils.asyncSend(RMQConstant.Topics.RLT_TEST_TOPIC,RMQConstant.Tags.TAG_4 ,tdGoods);

    }

    @Override
    public List<TdGoods> getByName(TdGoods tdGoods) throws Exception {
        Thread.sleep(1000);
        return goodsDao.getByName(tdGoods);
    }

    @Override
    public List<TdGoods> getById(List<Long> id) throws Exception {

        return goodsDao.getById(id);
    }
}
