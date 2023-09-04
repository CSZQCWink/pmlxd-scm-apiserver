package com.sungeon.bos.mapper;

import com.sungeon.bos.entity.base.SupplierEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-8-9
 **/
@Mapper
public interface ISupplierMapper {

	SupplierEntity querySupplierByCode(String supplierCode);

	Long querySupplierIdByCode(String supplierCode);
	Long addSupplier(SupplierEntity supplierEntity);

	Long addSupplierList(@Param("list") List<SupplierEntity> supplierEntityList);
}
