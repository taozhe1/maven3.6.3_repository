package com.redis.redis_springboot.bean;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "td_goods")
public class TdGoods {


    /**
     * 订单id
     */
    private Long id;

    /**
     * 总量
     */
    private Integer total;

    /**
     * 商品名字
     */
    private String goodsName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 商品类型
     * 1-洗漱类
     * 2-
     */
    private String type;

    /**
     * 商品状态
     * 1-上架
     * 0-下架
     */
    private String status;

    /**
     * 创建人
     */
    private String userName;

    /**
     * 创建人手机号
     */

    @JsonProperty("PHONE_NUMBER")
    private Integer phoneNumber;










}
