package com.redis.redis_springboot.confign;


import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


@Data
@Component
//@ConfigurationProperties(prefix = "aliyun.oss.file")
public class ConstantPropertiesConfig implements InitializingBean {


    //读取配置文件的内容

    private String endpoint;
    private String keyId;
    private String keySecret;
    private String bucketName;

    @Override
    public void afterPropertiesSet() throws Exception {
        //从环境变量中取值
        endpoint = System.getenv("OSS_ENDPOINT");
        keyId = System.getenv("OSS_ACCESS_KEY_ID");
        keySecret = System.getenv("OSS_ACCESS_KEY_SECRET");
        bucketName = System.getenv("OSS_BUCKET_NAME");
    }

}