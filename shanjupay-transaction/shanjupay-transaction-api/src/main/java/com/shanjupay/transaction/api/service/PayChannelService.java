package com.shanjupay.transaction.api.service;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.transaction.api.dto.PayChannelDTO;
import com.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.shanjupay.transaction.api.dto.PlatformChannelDTO;

import java.util.List;

/**
 * 描述 支付渠道服务 管理平台支付渠道，原始支付渠道，以及相关配置
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/23 21:49
 **/
public interface PayChannelService {
    /**
     * 获取平台服务类型
     * @return
     * @throws BusinessException
     */
    List<PlatformChannelDTO> queryPlatformChannel() throws BusinessException;

    /**
     * 为某个服务绑定应用
     * @param appId
     * @param platformChannel
     * @throws BusinessException
     */
    void bindPlatformChannelForApp(String appId, String platformChannel) throws BusinessException;

    /**
     * 应用绑定服务类型的状态
     * @param appId
     * @param platformChannel
     * @return 已绑定 1,否则0
     * @throws BusinessException
     */
    int queryAppBindPlatformChannel(String appId,String platformChannel)throws BusinessException;

    /**
     * 根据平台服务类型获取支付渠道列表
     * @param platformChannelCode
     * @return
     */
    List<PayChannelDTO> queryPayChannelByPlatformChannel(String platformChannelCode);

    /**
     * 保存支付渠道参数
     * @param payChannelParam 商户原始支付渠道参数
     * @throws BusinessException
     */
    void savePayChannelParam(PayChannelParamDTO payChannelParam) throws BusinessException;

    /**
     * 获取指定应用指定服务类型下所包含的原始支付渠道参数列表
     * @param appId 应用id
     * @param platformChannel
     * @return
     * @throws BusinessException 服务类型
     */
    List<PayChannelParamDTO> queryPayChannelParamByAppAndPlatform(String appId, String platformChannel) throws BusinessException;

    /**
     * 获取指定应用指定服务类型下所包含的某个原始支付参数
     * @param appId
     * @param platformChannel
     * @param payChannel
     * @return
     * @throws BusinessException
     */
    PayChannelParamDTO queryParamByAppPlatformAndPayChannel(String appId, String platformChannel, String payChannel) throws BusinessException;
}
