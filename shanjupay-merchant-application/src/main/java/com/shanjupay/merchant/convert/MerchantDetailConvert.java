package com.shanjupay.merchant.convert;


import com.shanjupay.merchant.VO.MerchantDetailVO;
import com.shanjupay.merchant.dto.MerchantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/23 10:25
 **/
@Mapper
public interface MerchantDetailConvert {
    MerchantDetailConvert INSTANCE = Mappers.getMapper(MerchantDetailConvert.class);

    /**
     * 对象转换
     * @param merchantDetailVO 表现层对象
     * @return 数据传输对象
     */
    MerchantDTO vo2dto(MerchantDetailVO merchantDetailVO);

    /**
     * 对象转换
     * @param merchantDTO 数据传输对象
     * @return 表现层对象
     */
    MerchantDetailVO dto2vo(MerchantDTO merchantDTO);

    /**
     * 对象转换
     * @param merchantDetailVOS 表现层对象集合
     * @return 数据传输对象集合
     */
    List<MerchantDTO> listvo2dto(List<MerchantDetailVO> merchantDetailVOS);

    /**
     * 对象转换
     * @param merchantDTOS 数据传输对象集合
     * @return 表现层对象集合
     */
    List<MerchantDetailVO> listdto2vo(List<MerchantDTO> merchantDTOS);



}
