package com.shanjupay.merchant.api;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.merchant.dto.AppDTO;

import java.util.List;

/**
 * 描述 商户下创建应用
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/23 13:47
 **/
public interface AppService {
    /**
     * 创建应用
     * @param merchantId 商户ID
     * @param appDTO 相关参数
     * @return AppDTO
     * @throws BusinessException
     */
    AppDTO createApp(Long merchantId,AppDTO appDTO) throws BusinessException;


    /**
     * 查询商户下的应用列表
     * @param merchantId
     * @return
     * @throws BusinessException
     */
    List<AppDTO> queryAppByMerchant(Long merchantId) throws BusinessException;

    /**
     * 通过业务id查询应用
     * @param appId
     * @return
     * @throws BusinessException
     */
    AppDTO getAppById(String appId) throws BusinessException;
}
