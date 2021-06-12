package com.shanjupay.transaction.convert;

import com.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.shanjupay.transaction.entity.PayChannelParam;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-05-30T23:25:39+0800",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class PayChannelParamConvertImpl implements PayChannelParamConvert {

    @Override
    public PayChannelParam dto2entity(PayChannelParamDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PayChannelParam payChannelParam = new PayChannelParam();

        payChannelParam.setId( dto.getId() );
        payChannelParam.setChannelName( dto.getChannelName() );
        payChannelParam.setMerchantId( dto.getMerchantId() );
        payChannelParam.setPayChannel( dto.getPayChannel() );
        payChannelParam.setParam( dto.getParam() );
        payChannelParam.setAppPlatformChannelId( dto.getAppPlatformChannelId() );

        return payChannelParam;
    }

    @Override
    public List<PayChannelParamDTO> listentity2listdto(List<PayChannelParam> PlatformChannel) {
        if ( PlatformChannel == null ) {
            return null;
        }

        List<PayChannelParamDTO> list = new ArrayList<PayChannelParamDTO>( PlatformChannel.size() );
        for ( PayChannelParam payChannelParam : PlatformChannel ) {
            list.add( payChannelParamToPayChannelParamDTO( payChannelParam ) );
        }

        return list;
    }

    @Override
    public List<PayChannelParam> listdto2listentity(List<PayChannelParamDTO> PlatformChannelDTO) {
        if ( PlatformChannelDTO == null ) {
            return null;
        }

        List<PayChannelParam> list = new ArrayList<PayChannelParam>( PlatformChannelDTO.size() );
        for ( PayChannelParamDTO payChannelParamDTO : PlatformChannelDTO ) {
            list.add( dto2entity( payChannelParamDTO ) );
        }

        return list;
    }

    protected PayChannelParamDTO payChannelParamToPayChannelParamDTO(PayChannelParam payChannelParam) {
        if ( payChannelParam == null ) {
            return null;
        }

        PayChannelParamDTO payChannelParamDTO = new PayChannelParamDTO();

        payChannelParamDTO.setId( payChannelParam.getId() );
        payChannelParamDTO.setChannelName( payChannelParam.getChannelName() );
        payChannelParamDTO.setMerchantId( payChannelParam.getMerchantId() );
        payChannelParamDTO.setPayChannel( payChannelParam.getPayChannel() );
        payChannelParamDTO.setParam( payChannelParam.getParam() );
        payChannelParamDTO.setAppPlatformChannelId( payChannelParam.getAppPlatformChannelId() );

        return payChannelParamDTO;
    }
}
