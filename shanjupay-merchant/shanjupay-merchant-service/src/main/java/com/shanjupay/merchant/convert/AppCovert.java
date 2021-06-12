package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.dto.AppDTO;
import com.shanjupay.merchant.entity.App;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 **/
@Mapper
public interface AppCovert {

    AppCovert INSTANCE = Mappers.getMapper(AppCovert.class);

    /**
     * 对象转换
     * @param entity
     * @return
     */
    AppDTO entity2dto(App entity);

    /**
     * 对象转换
     * @param dto
     * @return
     */
    App dto2entity(AppDTO dto);

    /**
     * 对象转换
     * @param app
     * @return
     */
    List<AppDTO> listentity2dto(List<App> app);

}
