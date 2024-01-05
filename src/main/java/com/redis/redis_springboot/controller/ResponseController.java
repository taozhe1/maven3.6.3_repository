package com.redis.redis_springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ResponseEntityTest")
@Controller
public class ResponseController {

    @RequestMapping("/holle")
    public ResponseEntity<?> holle(){
       return ResponseEntity.ok("holle");


    }

}
