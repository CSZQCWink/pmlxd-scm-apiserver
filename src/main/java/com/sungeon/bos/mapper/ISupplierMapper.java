package com.sungeon.bos.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author 刘国帅
 * @date 2019-8-9
 **/
@Mapper
public interface ISupplierMapper {

    Long querySupplierIdByCode(String supplierCode);

}
