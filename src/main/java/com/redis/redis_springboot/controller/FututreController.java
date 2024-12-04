package com.redis.redis_springboot.controller;


import com.redis.redis_springboot.bean.TdGoods;
import com.redis.redis_springboot.service.TdGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@RestController
@RequestMapping("/futureTest")
public class FututreController {

    @Autowired
    TdGoodsService goodsService;

    @RequestMapping("/test01")
    public Map<String, List<TdGoods>> test01(@RequestBody TdGoods tdGoods) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        Map<String, List<TdGoods>> map = new HashMap<>();
        long start = System.currentTimeMillis();


        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
            try {
                map.put("01",goodsService.getByName(tdGoods));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            },executorService), CompletableFuture.runAsync(() -> {
            try {
                map.put("02",goodsService.getByName(tdGoods));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        },executorService), CompletableFuture.runAsync(() -> {
            try {
                map.put("03",goodsService.getByName(tdGoods));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        },executorService));

        voidCompletableFuture.join();
        long end = System.currentTimeMillis();
        long l = end - start;
        System.out.println("耗时："+l);


        return map;
    }




}
