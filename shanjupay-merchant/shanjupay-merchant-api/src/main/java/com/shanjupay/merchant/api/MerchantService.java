package com.shanjupay.merchant.api;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.merchant.dto.MerchantDTO;
import com.shanjupay.merchant.dto.StaffDTO;
import com.shanjupay.merchant.dto.StoreDTO;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/16 15:16
 **/
public interface MerchantService {
    /**
     * 通过ID查询商品
     *
     * @param id
     * @return
     */
    MerchantDTO queryMerchantById(Long id);

    /**
     * 注册商户接口
     *
     * @param merchantDTO 商户注册信息
     * @return 注册成功的商户信息
     */
    MerchantDTO creatMerchant(MerchantDTO merchantDTO) throws BusinessException;

    /**
     * 资质申请接口
     *
     * @param id          id
     * @param merchantDTO 请求参数
     * @throws BusinessException
     */
    void applyMerchant(Long id, MerchantDTO merchantDTO) throws BusinessException;

    /**
     * 新增门店
     *
     * @param storeDTO
     * @return
     * @throws BusinessException
     */
    StoreDTO createStore(StoreDTO storeDTO) throws BusinessException;


    /**
     * 新增员工
     *
     * @param staffDTO
     * @return
     * @throws BusinessException
     */
    StaffDTO createStaff(StaffDTO staffDTO) throws BusinessException;

    /**
     * 为门店设置管理员
     *
     * @param storeId
     * @param staffId
     * @throws BusinessException
     */
    void bindStaffToStore(Long storeId, Long staffId) throws BusinessException;

    /**
     * 查询租户下的商户
     * @param tenantId
     * @return
     * @throws BusinessException
     */
    MerchantDTO queryMerchantByTenantId(Long tenantId) throws BusinessException;


}
