package com.shanjupay.merchant.service;

import org.springframework.stereotype.Service;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/18 21:27
 **/
@Service
public interface SmsService {
    /**
     * 发送短信验证码
     * @param phone 手机号
     * @return 验证码
     */
    String sendMsg(String phone);

    /**
     * 校验验证码
     * @param verificationCode 验证码
     * @param verificationKey 验证码的key
     */
    void checkVerifiyCode(String verificationCode,String verificationKey);


}
