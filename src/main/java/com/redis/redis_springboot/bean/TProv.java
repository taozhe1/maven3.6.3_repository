package com.redis.redis_springboot.bean;


import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "t_prov")
public class TProv implements Serializable {

    private String id;
    private String name;




}
