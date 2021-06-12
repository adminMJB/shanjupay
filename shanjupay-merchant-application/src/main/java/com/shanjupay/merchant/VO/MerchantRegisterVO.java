package com.shanjupay.merchant.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/19 20:54
 **/
@Data
@ApiModel(value = "MerchantRegisterVO",description = "商户注册信息")
public class MerchantRegisterVO {

    @ApiModelProperty("商户手机号")
    private String mobile;

    @ApiModelProperty("商户用户名")
    private String username;

    @ApiModelProperty("商户密码")
    private String password;

    @ApiModelProperty("验证码的key")
    private String verifiykey;

    @ApiModelProperty("验证码")
    private String verifiyCode;

}
