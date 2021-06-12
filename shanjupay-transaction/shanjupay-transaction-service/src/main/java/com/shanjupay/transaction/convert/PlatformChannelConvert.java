package com.shanjupay.transaction.convert;

import com.shanjupay.transaction.api.dto.PlatformChannelDTO;
import com.shanjupay.transaction.entity.PlatformChannel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;


/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/23 22:46
 **/
@Mapper
public interface PlatformChannelConvert {
    PlatformChannelConvert INSTANCE = Mappers.getMapper(PlatformChannelConvert.class);

    PlatformChannelDTO entity2dto(PlatformChannel entity);

    PlatformChannel dto2entity(PlatformChannelDTO dto);

    List<PlatformChannelDTO> listentity2listdto(List<PlatformChannel> PlatformChannel);


}
