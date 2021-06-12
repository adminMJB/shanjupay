package com.shanjupay.transaction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanjupay.common.cache.Cache;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.RedisUtil;
import com.shanjupay.transaction.api.dto.PayChannelDTO;
import com.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.shanjupay.transaction.api.dto.PlatformChannelDTO;
import com.shanjupay.transaction.api.service.PayChannelService;
import com.shanjupay.transaction.convert.PayChannelParamConvert;
import com.shanjupay.transaction.convert.PlatformChannelConvert;
import com.shanjupay.transaction.entity.AppPlatformChannel;
import com.shanjupay.transaction.entity.PayChannelParam;
import com.shanjupay.transaction.entity.PlatformChannel;
import com.shanjupay.transaction.mapper.AppPlatformChannelMapper;
import com.shanjupay.transaction.mapper.PayChannelParamMapper;
import com.shanjupay.transaction.mapper.PlatformChannelMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/23 21:57
 **/
@Service
public class PayChannelServiceImpl implements PayChannelService {

    @Autowired
    private PlatformChannelMapper platformChannelMapper;

    @Autowired
    private AppPlatformChannelMapper appPlatformChannelMapper;
    @Autowired
    private PayChannelParamMapper payChannelParamMapper;
    @Resource
    private Cache cache;


    /**
     * 获取平台服务类型
     *
     * @return
     * @throws BusinessException
     */
    @Override
    public List<PlatformChannelDTO> queryPlatformChannel() throws BusinessException {
        List<PlatformChannel> platformChannels = platformChannelMapper.selectList(null);
        return PlatformChannelConvert.INSTANCE.listentity2listdto(platformChannels);
    }

    /**
     * 为某个应用绑定服务
     *
     * @param appId
     * @param platformChannel
     * @throws BusinessException
     */
    @Override
    public void bindPlatformChannelForApp(String appId, String platformChannel) throws BusinessException {
        //先查询是否已经绑定
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new LambdaQueryWrapper<AppPlatformChannel>().eq(AppPlatformChannel::getAppId, appId)
                .eq(AppPlatformChannel::getPlatformChannel, platformChannel));
        if (appPlatformChannel == null) {
            AppPlatformChannel entry = new AppPlatformChannel();
            entry.setAppId(appId);
            entry.setPlatformChannel(platformChannel);
            appPlatformChannelMapper.insert(entry);
        }
    }

    @Override
    public int queryAppBindPlatformChannel(String appId, String platformChannel) throws BusinessException {
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new LambdaQueryWrapper<AppPlatformChannel>().eq(AppPlatformChannel::getAppId, appId)
                .eq(AppPlatformChannel::getPlatformChannel, platformChannel));
        if (appPlatformChannel == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public List<PayChannelDTO> queryPayChannelByPlatformChannel(String platformChannelCode) {
        return platformChannelMapper.selectPayChannelByPlatformChannel(platformChannelCode);
    }


    @Override
    public void savePayChannelParam(PayChannelParamDTO payChannelParamDTO) throws BusinessException {
        if (payChannelParamDTO == null ||
                StringUtils.isBlank(payChannelParamDTO.getAppId()) ||
                StringUtils.isBlank(payChannelParamDTO.getPlatformChannelCode()) ||
                StringUtils.isBlank(payChannelParamDTO.getPayChannel())) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        //根据appid和服务类型查询应用与服务类型绑定id
        Long appPlatformChannelId = selectIdByAppPlatformChannel(payChannelParamDTO.getAppId(), payChannelParamDTO.getPlatformChannelCode());
        if (appPlatformChannelId == null) {
            //应用未绑定该服务类型不可进行支付渠道参数配置
            throw new BusinessException(CommonErrorCode.E_300010);
        }
        //根据应用与服务类型绑定id和支付渠道查询参数信息
        PayChannelParam payChannelParam = payChannelParamMapper.selectOne(new LambdaQueryWrapper<PayChannelParam>().
                eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId).
                eq(PayChannelParam::getPayChannel, payChannelParamDTO.getPayChannel()));
        //更新已有配置
        if (payChannelParam != null) {
            payChannelParam.setChannelName(payChannelParamDTO.getChannelName());
            payChannelParam.setParam(payChannelParamDTO.getParam());
            payChannelParamMapper.updateById(payChannelParam);
        } else {
            //添加新配置
            PayChannelParam entity = PayChannelParamConvert.INSTANCE.dto2entity(payChannelParamDTO);
            entity.setId(null);
            //应用与服务类型绑定id
            entity.setAppPlatformChannelId(appPlatformChannelId);
            payChannelParamMapper.insert(entity);
        }

        //保存到redis中
        updateCach(payChannelParamDTO.getAppId(),payChannelParamDTO.getPlatformChannelCode());
    }

    /**
     * 查询支付渠道参数
     *
     * @param appId           应用id
     * @param platformChannel
     * @return
     * @throws BusinessException
     */
    @Override
    public List<PayChannelParamDTO> queryPayChannelParamByAppAndPlatform(String appId, String platformChannel) throws BusinessException {
        //先判断存在不存在
        String redisKey = RedisUtil.keyBuilder(appId, platformChannel);
        Boolean exists = cache.exists(redisKey);
        if (exists){
            String PayChannelParamDTO_String = cache.get(redisKey);
            List<PayChannelParamDTO> payChannelParamDTOS = JSON.parseArray(PayChannelParamDTO_String, PayChannelParamDTO.class);
            return payChannelParamDTOS;
        }


        //查出应用id和服务类型代码在app_platform_channel主键
        Long appPlatformChannelId = selectIdByAppPlatformChannel(appId, platformChannel);
        //根据appPlatformChannelId从pay_channel_param查询所有支付参数
        List<PayChannelParam> payChannelParams = payChannelParamMapper.selectList(new LambdaQueryWrapper<PayChannelParam>()
                .eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId));
        List<PayChannelParamDTO> payChannelParamDTOS = PayChannelParamConvert.INSTANCE.listentity2listdto(payChannelParams);
        updateCach(appId,platformChannel);
        return payChannelParamDTOS;
    }

    /**
     *
     * @param appId
     * @param platformChannel
     * @param payChannel
     * @return
     * @throws BusinessException
     */
    @Override
    public PayChannelParamDTO queryParamByAppPlatformAndPayChannel(String appId, String platformChannel, String payChannel) throws BusinessException {
        List<PayChannelParamDTO> payChannelParamDTOS = queryPayChannelParamByAppAndPlatform(appId, platformChannel);
        for (PayChannelParamDTO payChannelParam : payChannelParamDTOS) {
            if (payChannelParam.getPayChannel().equals(payChannel)) {
                return payChannelParam;
            }
        }
        return null;
    }

    /**
     * 根据appid和服务类型查询应用与服务类型绑定id
     *
     * @param appId
     * @param platformChannelCode
     * @return
     */
    private Long selectIdByAppPlatformChannel(String appId, String platformChannelCode) {
        //根据appid和服务类型查询应用与服务类型绑定id
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new LambdaQueryWrapper<AppPlatformChannel>()
                .eq(AppPlatformChannel::getAppId, appId)
                .eq(AppPlatformChannel::getPlatformChannel, platformChannelCode));
        if (appPlatformChannel != null) {
            return appPlatformChannel.getId();
        }
        return null;
    }

    /**
     * 根据应用id和服务类型code将查询到的参数配置列表写入redis中
     * @param appId
     * @param platformChannlCode
     */
    private void updateCach(String appId,String platformChannlCode) {
        //得到redis中key(付渠道参数配置列表的key)
        //格式：SJ_PAY_PARAM:应用id:服务类型code，例如：SJ_PAY_PARAM：ebcecedd-3032-49a6-9691-4770e66577af：shanju_c2b
        String redisKey = RedisUtil.keyBuilder(appId, platformChannlCode);
        //根据key查询redis
        Boolean exists = cache.exists(redisKey);
        if (exists) {
            cache.del(redisKey);
        }
        //根据应用id和服务类型code查询支付渠道参数
        //根据应用和服务类型找到它们绑定id
        Long appPlatformChannelId = selectIdByAppPlatformChannel(appId, platformChannlCode);
        if (appPlatformChannelId != null) {
            //应用和服务类型绑定id查询支付渠道参数记录
            List<PayChannelParam> payChannelParams = payChannelParamMapper.selectList(new LambdaQueryWrapper<PayChannelParam>().eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId));
            List<PayChannelParamDTO> payChannelParamDTOS = PayChannelParamConvert.INSTANCE.listentity2listdto(payChannelParams);
            //将payChannelParamDTOS转成json串存入redis
            cache.set(redisKey, JSON.toJSON(payChannelParamDTOS).toString());
        }
    }
}
