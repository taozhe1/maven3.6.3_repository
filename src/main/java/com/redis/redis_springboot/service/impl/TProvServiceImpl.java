package com.redis.redis_springboot.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.redis_springboot.bean.TProv;
import com.redis.redis_springboot.dao.TProvDao;
import com.redis.redis_springboot.service.TProvService;
import org.springframework.stereotype.Service;
@Service
public class TProvServiceImpl extends ServiceImpl<TProvDao, TProv> implements TProvService {

}
