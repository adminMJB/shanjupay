package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.VO.MerchantRegisterVO;
import com.shanjupay.merchant.dto.MerchantDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-13T13:59:29+0800",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class MerchantRegisterConvertImpl implements MerchantRegisterConvert {

    @Override
    public MerchantDTO vo2dto(MerchantRegisterVO merchant) {
        if ( merchant == null ) {
            return null;
        }

        MerchantDTO merchantDTO = new MerchantDTO();

        merchantDTO.setPassword( merchant.getPassword() );
        merchantDTO.setUsername( merchant.getUsername() );
        merchantDTO.setMobile( merchant.getMobile() );

        return merchantDTO;
    }

    @Override
    public MerchantRegisterVO dto2vo(MerchantDTO merchantDTO) {
        if ( merchantDTO == null ) {
            return null;
        }

        MerchantRegisterVO merchantRegisterVO = new MerchantRegisterVO();

        merchantRegisterVO.setMobile( merchantDTO.getMobile() );
        merchantRegisterVO.setUsername( merchantDTO.getUsername() );
        merchantRegisterVO.setPassword( merchantDTO.getPassword() );

        return merchantRegisterVO;
    }

    @Override
    public List<MerchantDTO> listVo2dto(List<MerchantRegisterVO> merchants) {
        if ( merchants == null ) {
            return null;
        }

        List<MerchantDTO> list = new ArrayList<MerchantDTO>( merchants.size() );
        for ( MerchantRegisterVO merchantRegisterVO : merchants ) {
            list.add( vo2dto( merchantRegisterVO ) );
        }

        return list;
    }

    @Override
    public List<MerchantRegisterVO> listDto2vo(List<MerchantDTO> merchantDTOS) {
        if ( merchantDTOS == null ) {
            return null;
        }

        List<MerchantRegisterVO> list = new ArrayList<MerchantRegisterVO>( merchantDTOS.size() );
        for ( MerchantDTO merchantDTO : merchantDTOS ) {
            list.add( dto2vo( merchantDTO ) );
        }

        return list;
    }
}
