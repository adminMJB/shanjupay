package com.shanjupay.transaction.convert;

import com.shanjupay.transaction.api.dto.OrderResultDTO;
import com.shanjupay.transaction.entity.PayOrder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/23 22:58
 **/
@Mapper
public interface OrderResultConvert {

    OrderResultConvert INSTANCE = Mappers.getMapper(OrderResultConvert.class);

    OrderResultDTO entity2dto(PayOrder entity);

    PayOrder dto2entity(OrderResultDTO dto);
}
