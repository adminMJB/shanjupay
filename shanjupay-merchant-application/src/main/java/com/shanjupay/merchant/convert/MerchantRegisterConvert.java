package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.VO.MerchantRegisterVO;
import com.shanjupay.merchant.dto.MerchantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 将商户注册vo和dto进行转换
 * Created by Administrator.
 * @author Administrator
 */
@Mapper
public interface MerchantRegisterConvert {

    MerchantRegisterConvert INSTANCE = Mappers.getMapper(MerchantRegisterConvert.class);

    /**
     * 商品传输对象转换
     * @param merchant 持久层对象
     * @return 服务层对象
     */
    MerchantDTO vo2dto(MerchantRegisterVO merchant);

    /**
     * 商品对象转换
     * @param merchantDTO 业务层对象
     * @return 持久层对象
     */
    MerchantRegisterVO dto2vo(MerchantDTO merchantDTO);

    /**
     * 商品集合转换
     * @param merchants 业务层集合对象
     * @return 持久层集合对象
     */
    List<MerchantDTO> listVo2dto(List<MerchantRegisterVO> merchants);

    /**商品集合转换
     *
     * @param merchantDTOS 业务层集合对象
     * @return 持久层集合对象
     */
    List<MerchantRegisterVO> listDto2vo(List<MerchantDTO> merchantDTOS);

}
