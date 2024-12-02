package com.redis.redis_springboot.bean;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author macbook
 */
@Data
@Table(name = "t_user")
public class TUser implements Serializable {

    private int id;
    private String userName;
    private String password;
    private int lastLogin;
    private String lastIp;
    private int status;
    private String salt;
    private String picture;

}
