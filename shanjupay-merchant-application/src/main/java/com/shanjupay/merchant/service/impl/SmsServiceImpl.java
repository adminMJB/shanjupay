package com.shanjupay.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.merchant.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/18 21:30
 **/
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    /**
     * 注入restTemplate
     */
    @Autowired
    private RestTemplate restTemplate;
    /**
     * 注入url
     */
    @Value("${sms.url}")
    private String url;
    @Value("${sms.effectiveTime}")
    private String effectiveTime;

    @Override
    public String sendMsg(String phone) {
        String sms_url = url+"/generate?name=sms&effectiveTime="+effectiveTime;
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("mobile",phone);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> exchange = null;
        try {
            exchange = restTemplate.exchange(sms_url, HttpMethod.POST, new HttpEntity(bodyMap,httpHeaders), Map.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RuntimeException("获取验证码失败");
        }
        Map responseMap = exchange.getBody();
        if (null == responseMap || null == responseMap.get("result")){
            throw new RuntimeException("获取验证码失败");
        }
        Map result = (Map) responseMap.get("result");
        String  key =result.get("key").toString();
        log.info("得到发送的验证码对应的key{}",key);
        return key;
    }

    /**
     * 校验验证码
     * @param verificationCode 验证码
     * @param verificationKey 验证码的key
     * @throws BusinessException
     */
    @Override
    public void checkVerifiyCode(String verificationCode, String verificationKey) throws BusinessException {
        String sms_url = url+"/verify?name=sms&verificationCode="+verificationCode+"&verificationKey="+verificationKey;
        ResponseEntity<Map> exchange = null;
        try {
             exchange = restTemplate.exchange(sms_url, HttpMethod.POST, HttpEntity.EMPTY, Map.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100102);
        }
        Map responseMap = exchange.getBody();
        if (null == responseMap || null == responseMap.get("result") || !(Boolean) responseMap.get("result")){
            throw new BusinessException(CommonErrorCode.E_100102);
        }

    }
}
