package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.BaseMessage;
import com.tencent.wxcloudrun.service.BabyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Map;

/**
 * counter控制器
 */
@RestController

public class CounterController {

    final CounterService counterService;
    final BabyService babyService;
    final Logger logger;

    public CounterController(@Autowired CounterService counterService, @Autowired BabyService babyService) {
        this.counterService = counterService;
        this.babyService = babyService;
        this.logger = LoggerFactory.getLogger(CounterController.class);
    }

    @GetMapping(value = "/api")
    String validToken(@RequestParam String echostr) {
        return echostr;
    }

    @PostMapping(value = "/api", consumes = {MediaType.TEXT_XML_VALUE})
    String receiveMsg(@RequestBody BaseMessage body) {
        String content = body.getContent();
        logger.info(content);
        if ("1".equals(content.trim()) || content.startsWith("1,")){
            String[] split = content.split(",");
            if(split.length==1){

                babyService.newRecord(null);
            }else{
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    babyService.newRecord(format.parse(split[1]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return "success";
        }
        return "ignore this msg";
    }

    /**
     * 获取当前计数
     *
     * @return API response json
     */
    @GetMapping(value = "/api/count")
    ApiResponse get() {
        logger.info("/api/count get request");
        Optional<Counter> counter = counterService.getCounter(1);
        Integer count = 0;
        if (counter.isPresent()) {
            count = counter.get().getCount();
        }

        return ApiResponse.ok(count);
    }


    /**
     * 更新计数，自增或者清零
     *
     * @param request {@link CounterRequest}
     * @return API response json
     */
    @PostMapping(value = "/api/count")
    ApiResponse create(@RequestBody CounterRequest request) {
        logger.info("/api/count post request, action: {}", request.getAction());

        Optional<Counter> curCounter = counterService.getCounter(1);
        if (request.getAction().equals("inc")) {
            Integer count = 1;
            if (curCounter.isPresent()) {
                count += curCounter.get().getCount();
            }
            Counter counter = new Counter();
            counter.setId(1);
            counter.setCount(count);
            counterService.upsertCount(counter);
            return ApiResponse.ok(count);
        } else if (request.getAction().equals("clear")) {
            if (!curCounter.isPresent()) {
                return ApiResponse.ok(0);
            }
            counterService.clearCount(1);
            return ApiResponse.ok(0);
        } else {
            return ApiResponse.error("参数action错误");
        }
    }

}