package com.redis.redis_springboot.controller;


import com.redis.redis_springboot.util.AliyunOssUtil;
import com.redis.redis_springboot.util.FrontResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/files")
public class FileController {


    @Autowired
    private AliyunOssUtil aliyunOssUtil;


    @RequestMapping("/upload")
    public FrontResult upload(@RequestParam MultipartFile file)throws Exception {
        // 将 MultipartFile 转换为 File 对象
        // 上传到阿里云OSS，使用真实文件名
        aliyunOssUtil.putObject( aliyunOssUtil.convertMultipartFileToFile(file));

        return new FrontResult("成功");
    }


    @RequestMapping("/download")
    public void downloadFile(HttpServletResponse response, @RequestParam String fileName) throws Exception {
        aliyunOssUtil.downloadFile(response,fileName);
    }



}
