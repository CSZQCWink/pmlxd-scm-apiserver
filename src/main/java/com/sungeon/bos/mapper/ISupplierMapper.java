package com.sungeon.bos.mapper;

import com.sungeon.bos.entity.base.SupplierEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ISupplierMapper {

	SupplierEntity querySupplierByCode(@Param("supplierCode") String supplierCode);

	Long querySupplierIdByCode(@Param("supplierCode") String supplierCode);
	Long addSupplier(SupplierEntity supplierEntity);

	void callSupplierAC(@Param("supplierId") Long supplierId);
}
