package com.shanjupay.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.merchant.api.AppService;
import com.shanjupay.merchant.convert.AppCovert;
import com.shanjupay.merchant.dto.AppDTO;
import com.shanjupay.merchant.entity.App;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.mapper.AppMapper;
import com.shanjupay.merchant.mapper.MerchantMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/23 13:51
 **/
@Service
@Slf4j
public class AppServiceImpl implements AppService {
    /**
     * 注入appMapper
     */
    @Autowired
    private AppMapper appMapper;

    /**
     * 注入商户mapper
     */
    @Autowired
    private MerchantMapper merchantMapper;

    /**
     1）校验商户是否通过资质审核
     如果商户资质审核没有通过不允许创建应用。
     2）生成应用ID 应用Id使用UUID方式生成。
     3）保存商户应用信息 应用名称需要校验唯一性
     */
    /**
     * 创建商户
     * @param merchantId 商户ID
     * @param appDTO 相关参数
     * @return AppDTO
     */
    @Override
    public AppDTO createApp(Long merchantId, AppDTO appDTO) {
        //校验参数
        if (merchantId == null || appDTO == null || appDTO.getAppName() == null){
            throw new BusinessException(CommonErrorCode.E_110006);
        }
        //校验商户名是否重复
        if (existAppName(appDTO.getAppName())){
            throw new BusinessException(CommonErrorCode.E_200004);
        }
        //校验商户是否通过资质审核
        isApproved(merchantId);
        //生成应用ID 应用Id使用UUID方式生成。
        String appId = UUID.randomUUID().toString();
        App entry = AppCovert.INSTANCE.dto2entity(appDTO);
        entry.setId(appDTO.getId());
        entry.setAppId(appId);
        entry.setMerchantId(merchantId);
        appMapper.insert(entry);
        return AppCovert.INSTANCE.entity2dto(entry);
    }


    @Override
    public List<AppDTO> queryAppByMerchant(Long merchantId) throws BusinessException {
        List<App> apps = appMapper.selectList(new LambdaQueryWrapper<App>().eq(App::getMerchantId, merchantId));
        return AppCovert.INSTANCE.listentity2dto(apps);
    }

    @Override
    public AppDTO getAppById(String appId) throws BusinessException {
        App app = appMapper.selectOne(new LambdaQueryWrapper<App>().eq(App::getAppId, appId));
        return AppCovert.INSTANCE.entity2dto(app);
    }

    /**
     * 校验商户名称是否重复
     * @param appName 商户名称
     * @return Boolean
     */
    private Boolean existAppName(String appName) {
        Integer count = appMapper.selectCount(new LambdaQueryWrapper<App>().eq(App::getAppName, appName));
        return count > 0;
    }
    private void isApproved(Long merchantId){
        Merchant merchant = merchantMapper.selectById(merchantId);

        if (merchant == null){
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        if (!"2".equals(merchant.getAuditStatus())){
            throw new BusinessException(CommonErrorCode.E_200003);
        }
    }
}
