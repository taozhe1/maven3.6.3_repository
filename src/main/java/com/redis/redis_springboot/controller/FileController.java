package com.redis.redis_springboot.controller;


import com.redis.redis_springboot.util.AliyunOssUtil;
import com.redis.redis_springboot.util.FrontResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

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
    public void downloadFile(HttpServletResponse response, @RequestParam String fileName) throws IOException {
        // 从阿里云 OSS 获取文件流
        InputStream object = aliyunOssUtil.getObject(fileName);

        // 设置响应头，告诉客户端这是一个文件下载的请求
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // 将文件流写入响应输出流
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = object.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            // 关闭输入流
            object.close();
        }
    }


}
