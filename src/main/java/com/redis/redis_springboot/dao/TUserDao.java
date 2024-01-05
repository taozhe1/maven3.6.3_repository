package com.redis.redis_springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redis.redis_springboot.bean.TUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TUserDao extends BaseMapper<TUser> {


    List<TUser> getByName(String userName);

    TUser validate(String userName);
}
