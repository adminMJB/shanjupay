package com.shanjupay.merchant.controller;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.PhoneUtil;
import com.shanjupay.common.util.StringUtil;
import com.shanjupay.merchant.VO.MerchantDetailVO;
import com.shanjupay.merchant.VO.MerchantRegisterVO;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.common.util.SecurityUtil;
import com.shanjupay.merchant.convert.MerchantDetailConvert;
import com.shanjupay.merchant.convert.MerchantRegisterConvert;
import com.shanjupay.merchant.dto.MerchantDTO;
import com.shanjupay.merchant.service.FileService;
import com.shanjupay.merchant.service.SmsService;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/16 15:40
 **/
@RestController
@Api(value = "商户平台的API", description = "商户平台的API")
public class MerchantController {
    @Reference
    private MerchantService merchantService;
    /**
     * 注入本地短信发送service
     */
    @Autowired
    private SmsService smsService;
    /**
     * 注入文件上传服务
     */
    @Autowired
    private FileService fileService;

    /**
     * 通过id查询商品
     *
     * @param id id
     * @return MerchantDTO
     */
    @GetMapping("/merchants/{id}")
    @ApiOperation(value = "根据id查询商户信息")
    public MerchantDTO queryMerchantById(@PathVariable("id") Long id) {
        MerchantDTO merchantDTO = merchantService.queryMerchantById(id);


        return merchantDTO;
    }

    @ApiOperation("获取登录用户的商户信息")
    @GetMapping(value = "/my/merchants")
    public MerchantDTO getMyMerchantInfo() {
        Long merchantId = SecurityUtil.getMerchantId();
        MerchantDTO merchant = merchantService.queryMerchantById(merchantId);
        return merchant;
    }

    /**
     * 获取手机验证码
     *
     * @return String
     */
    @GetMapping("/sms")
    @ApiOperation(value = "获取手机验证码")
    public String getMsmCode(@RequestParam("phone") String phone) {
        if (StringUtil.isEmpty(phone)) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        return smsService.sendMsg(phone);
    }

    /**
     * 商户注册
     *
     * @param merchantRegisterVO 请求参数
     * @return MerchantRegisterVO
     */
    @ApiOperation("注册商户")
    @PostMapping("/merchants/register")
    @ApiImplicitParam(name = "merchantRegister", value = "注册信息", required = true, dataType = "MerchantRegisterVO", paramType = "body")
    public MerchantRegisterVO registerMerchant(@RequestBody MerchantRegisterVO merchantRegisterVO) throws BusinessException {
        //参数校验
        if (null == merchantRegisterVO) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        if (StringUtil.isEmpty(merchantRegisterVO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        if (!PhoneUtil.isMatches(merchantRegisterVO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        //校验验证码
        smsService.checkVerifiyCode(merchantRegisterVO.getVerifiyCode(), merchantRegisterVO.getVerifiykey());
        //对象转换
        MerchantDTO merchantDTO = MerchantRegisterConvert.INSTANCE.vo2dto(merchantRegisterVO);
        //保存商户
        merchantService.creatMerchant(merchantDTO);
        return merchantRegisterVO;
    }

    //上传证件照
    @ApiOperation("上传证件照")
    @PostMapping("/upload")
    public String upload(@ApiParam(value = "证件照", required = true) @RequestParam("file") MultipartFile multipartFile) throws IOException {
        //调用fileService上传文件
        //生成的文件名称fileName，要保证它的唯一
        //文件原始名称
        String originalFilename = multipartFile.getOriginalFilename();
        //扩展名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") - 1);
        //文件名称
        String fileName = UUID.randomUUID() + suffix;
        //byte[] bytes,String fileName
        return fileService.upload(multipartFile.getBytes(), fileName);
    }

    /**
     * 资质申请
     *
     * @param merchantInfo
     */
    @ApiOperation("资质申请")
    @PostMapping("/my/merchants/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantInfo", value = "商户认证资料", required = true, dataType = "MerchantDetailVO", paramType = "body")
    })
    public void saveMerchant(@RequestBody MerchantDetailVO merchantInfo) {
        if (merchantInfo == null) {
            throw new BusinessException(CommonErrorCode.E_110006);
        }
        //解析Tooken
        Long merchantId = SecurityUtil.getMerchantId();
        //对象转换
        MerchantDTO merchantDTO = MerchantDetailConvert.INSTANCE.vo2dto(merchantInfo);
        merchantDTO.setId(merchantId);
        merchantService.applyMerchant(merchantId, merchantDTO);
    }
}
