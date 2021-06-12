package com.shanjupay.merchant;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpHead;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/18 20:13
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RestTemplateTest {
    @Autowired
    RestTemplate restTemplate;
    @Test
    public void test01(){
        String url = "http://www.baidu.com/";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String body = forEntity.getBody();
        System.out.println(body);
    }

    @Test
    public void Test02(){
        String url = "http://localhost:56085/sailing/generate?effectiveTime=600&name=sms";
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("mobile","18205675215");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(bodyMap,httpHeaders);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map body = exchange.getBody();
        log.info("发送验证码成功{}", JSON.toJSON(exchange));
        System.out.println(body);
        Map result = (Map) body.get("result");
        String  key = (String) result.get("key");
        System.out.println(key+"+++++++++++++++++++++");


    }



}
