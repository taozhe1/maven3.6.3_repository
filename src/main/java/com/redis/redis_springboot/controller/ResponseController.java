package com.redis.redis_springboot.controller;

import com.redis.redis_springboot.util.FrontResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/ResponseEntityTest")
@RestController
public class ResponseController {

    @Value("${project.url.is}")
    private String url;

    @Value("${project.url.ip:12301}")
    private String ip;

    @RequestMapping("/holle")
    public ResponseEntity<?> holle(HttpServletRequest request){

        HttpSession session = request.getSession(false);
        System.out.println("getId==" +  session.getId());
        System.out.println("getMaxInactiveInterval=="+session.getMaxInactiveInterval());
        System.out.println("isNew==" +  session.isNew());
        return ResponseEntity.ok("holle");
    }

    @RequestMapping("/getSession")
    public ResponseEntity<?> getSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return ResponseEntity.ok("失效");
    }

    /**
     * 测试全局异常
     * @param a
     * @return
     * @throws Exception
     */
    @RequestMapping("/exceptionDispose")
    public FrontResult exceptionDispose(@RequestParam Integer a)  throws Exception{

        Map<Object, Object> hashMap = new HashMap<>();
        hashMap.put("a","1");
        hashMap.put("b","2");

        if(a==null){
        throw new Exception("a==null异常了");
        }
        if(a==2){
        throw new Exception("a==2异常了");
        }

        try {
            if(a==3){
                throw new Exception("a==3异常了");
            }
        }catch (Exception e){
            return FrontResult.getExceptionResult(null,e.toString());
        }
        return new FrontResult(hashMap);
    }


    /**
     * 局限于当前类
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    //@ResponseBody
    public FrontResult handleException(Exception e) {
        System.out.println("异常信息："+e);
        // 处理异常
        return FrontResult.getExceptionResult(null,e.toString());
    }

    @RequestMapping("/url")
    public void url(){
        System.out.println(ip);
        System.out.println(url);

    }



}
