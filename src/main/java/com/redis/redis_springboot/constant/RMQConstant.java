package com.redis.redis_springboot.constant;

import org.springframework.stereotype.Component;

/**
 * 常量类
 * @author macbook
 */

@Component
public interface RMQConstant {

    /**
     * 组
     */
       interface   Groups{

        String TEST_GROUP ="test-group";

    }


    /**
     * 主题
     */
      interface Topics{
          String RLT_TEST_TOPIC = "RLT_TEST_TOPIC";
    }


    /**
     * 标签
     */
    interface Tags{
        String TAG_1 = "tag1";

        String TAG_2 = "tag2";

        String TAG_3 = "tag3";

        String TAG_4 = "tag4";
    }


}
