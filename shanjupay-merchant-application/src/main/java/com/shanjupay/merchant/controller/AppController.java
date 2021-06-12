package com.shanjupay.merchant.controller;

import com.shanjupay.merchant.api.AppService;
import com.shanjupay.merchant.common.util.SecurityUtil;
import com.shanjupay.merchant.dto.AppDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述 商户平台‐应用管理
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/23 15:13
 **/
@Api(value = "商户平台‐应用管理", tags = "商户平台‐应用相关", description = "商户平台‐应用相关")
@RestController
public class AppController {
    /**
     * 远程调用dubbo服务
     */
    @Reference
    private AppService appService;

    /**
     * 商户创建应用
     * @param appDTO appDTO
     * @return AppDTO
     */
    @ApiOperation("商户创建应用")
    @ApiImplicitParams({ @ApiImplicitParam(name = "app", value = "应用信息", required = true, dataType = "AppDTO", paramType = "body")})
    @PostMapping(value = "/my/apps")
    public AppDTO createApp(@RequestBody AppDTO appDTO) {
        //获取登录信息
        Long merchantId = SecurityUtil.getMerchantId();
        return appService.createApp(merchantId,appDTO);
    }

    /**查询商户下的应用列表
     *
     * @return List<AppDTO>
     */
    @ApiOperation("查询商户下的应用列表")
    @GetMapping(value = "/my/apps")
    public List<AppDTO> queryMyApps() {
        Long merchantId = SecurityUtil.getMerchantId();
        return appService.queryAppByMerchant(merchantId);
    }

    /**
     * 根据appid获取应用的详细信息
     * @param appId
     * @return AppDTO
     */
    @ApiOperation("根据appid获取应用的详细信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "商户应用id", required = true, dataType = "String", paramType = "path")})
    @GetMapping(value = "/my/apps/{appId}")
    public AppDTO getApp(@PathVariable("appId") String appId){
        return appService.getAppById(appId);
    }


}
