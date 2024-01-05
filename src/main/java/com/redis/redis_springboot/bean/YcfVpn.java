package com.redis.redis_springboot.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author macbook
 */

@Table(name = "ycf_vpn")
@Data  //set，get，tostring
public class YcfVpn implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private int id;

    /**
     * 创建时间
     */
    private Date createTime;





}
