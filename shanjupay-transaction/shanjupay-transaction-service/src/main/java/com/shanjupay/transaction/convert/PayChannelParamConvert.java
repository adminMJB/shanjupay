package com.shanjupay.transaction.convert;

import com.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.shanjupay.transaction.entity.PayChannelParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/23 22:59
 **/
@Mapper
public interface PayChannelParamConvert {
    PayChannelParamConvert INSTANCE= Mappers.getMapper(PayChannelParamConvert.class);

    PayChannelParam dto2entity(PayChannelParamDTO dto);

    List<PayChannelParamDTO> listentity2listdto(List<PayChannelParam> PlatformChannel);

    List<PayChannelParam> listdto2listentity(List<PayChannelParamDTO> PlatformChannelDTO);

}
