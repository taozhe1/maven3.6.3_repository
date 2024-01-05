package com.redis.redis_springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redis.redis_springboot.bean.TUser;

import java.util.List;

public interface TUserService extends IService<TUser> {


    List<TUser> getByName(String userName);
}
