package com.sungeon.bos.mapper;

import com.sungeon.bos.entity.base.SupplierEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ISupplierMapper {

	SupplierEntity querySupplierByCode(String supplierCode);

	Long querySupplierIdByCode(String supplierCode);
	Long addSupplier(SupplierEntity supplierEntity);

}
