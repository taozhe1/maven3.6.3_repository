package com.redis.redis_springboot.service.impl;

import com.redis.redis_springboot.bean.TUser;
import com.redis.redis_springboot.dao.TUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TUserDao tUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TUser user = tUserDao.validate(username);
        if(user == null){
            throw new  UsernameNotFoundException("username is null");
        }
        return new User(user.getUserName(), user.getPassword(), Collections.emptyList());
    }
}
