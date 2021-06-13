package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.VO.MerchantDetailVO;
import com.shanjupay.merchant.dto.MerchantDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-13T13:59:29+0800",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class MerchantDetailConvertImpl implements MerchantDetailConvert {

    @Override
    public MerchantDTO vo2dto(MerchantDetailVO merchantDetailVO) {
        if ( merchantDetailVO == null ) {
            return null;
        }

        MerchantDTO merchantDTO = new MerchantDTO();

        merchantDTO.setMerchantName( merchantDetailVO.getMerchantName() );
        merchantDTO.setMerchantNo( merchantDetailVO.getMerchantNo() );
        merchantDTO.setMerchantAddress( merchantDetailVO.getMerchantAddress() );
        merchantDTO.setMerchantType( merchantDetailVO.getMerchantType() );
        merchantDTO.setBusinessLicensesImg( merchantDetailVO.getBusinessLicensesImg() );
        merchantDTO.setIdCardFrontImg( merchantDetailVO.getIdCardFrontImg() );
        merchantDTO.setIdCardAfterImg( merchantDetailVO.getIdCardAfterImg() );
        merchantDTO.setUsername( merchantDetailVO.getUsername() );
        merchantDTO.setContactsAddress( merchantDetailVO.getContactsAddress() );

        return merchantDTO;
    }

    @Override
    public MerchantDetailVO dto2vo(MerchantDTO merchantDTO) {
        if ( merchantDTO == null ) {
            return null;
        }

        MerchantDetailVO merchantDetailVO = new MerchantDetailVO();

        merchantDetailVO.setMerchantName( merchantDTO.getMerchantName() );
        merchantDetailVO.setMerchantNo( merchantDTO.getMerchantNo() );
        merchantDetailVO.setMerchantAddress( merchantDTO.getMerchantAddress() );
        merchantDetailVO.setMerchantType( merchantDTO.getMerchantType() );
        merchantDetailVO.setBusinessLicensesImg( merchantDTO.getBusinessLicensesImg() );
        merchantDetailVO.setIdCardFrontImg( merchantDTO.getIdCardFrontImg() );
        merchantDetailVO.setIdCardAfterImg( merchantDTO.getIdCardAfterImg() );
        merchantDetailVO.setUsername( merchantDTO.getUsername() );
        merchantDetailVO.setContactsAddress( merchantDTO.getContactsAddress() );

        return merchantDetailVO;
    }

    @Override
    public List<MerchantDTO> listvo2dto(List<MerchantDetailVO> merchantDetailVOS) {
        if ( merchantDetailVOS == null ) {
            return null;
        }

        List<MerchantDTO> list = new ArrayList<MerchantDTO>( merchantDetailVOS.size() );
        for ( MerchantDetailVO merchantDetailVO : merchantDetailVOS ) {
            list.add( vo2dto( merchantDetailVO ) );
        }

        return list;
    }

    @Override
    public List<MerchantDetailVO> listdto2vo(List<MerchantDTO> merchantDTOS) {
        if ( merchantDTOS == null ) {
            return null;
        }

        List<MerchantDetailVO> list = new ArrayList<MerchantDetailVO>( merchantDTOS.size() );
        for ( MerchantDTO merchantDTO : merchantDTOS ) {
            list.add( dto2vo( merchantDTO ) );
        }

        return list;
    }
}
