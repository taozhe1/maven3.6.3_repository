package com.redis.redis_springboot.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redis.redis_springboot.bean.TUser;
import com.redis.redis_springboot.dao.TUserDao;
import com.redis.redis_springboot.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TUserServiceImpl extends ServiceImpl<TUserDao, TUser> implements TUserService {

    @Autowired
    private TUserDao tUserDao;





    @Override
    public List<TUser> getByName(String userName) {

        return tUserDao.getByName(userName);
    }



}
