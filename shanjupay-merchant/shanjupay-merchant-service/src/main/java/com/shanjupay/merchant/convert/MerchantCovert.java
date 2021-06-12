package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.dto.MerchantDTO;
import com.shanjupay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 描述 传输对象转换
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/20 19:34
 **/
@Mapper
public interface MerchantCovert {
    MerchantCovert INSTANCE = Mappers.getMapper(MerchantCovert.class);

    /**
     * 商品传输对象转换
     * @param merchant 持久层对象
     * @return 服务层对象
     */
    MerchantDTO entry2dto(Merchant merchant);

    /**
     * 商品对象转换
     * @param merchantDTO 业务层对象
     * @return 持久层对象
     */
    Merchant dto2entry(MerchantDTO merchantDTO);

    /**
     * 商品集合转换
     * @param merchants 业务层集合对象
     * @return 持久层集合对象
     */
    List<MerchantDTO> listEntry2dto(List<Merchant> merchants);

    /**商品集合转换
     *
     * @param merchantDTOS 业务层集合对象
     * @return 持久层集合对象
     */
    List<Merchant> listDto2entry(List<MerchantDTO> merchantDTOS);




}
