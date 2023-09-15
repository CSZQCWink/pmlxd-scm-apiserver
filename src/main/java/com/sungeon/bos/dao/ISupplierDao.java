package com.sungeon.bos.dao;

import com.sungeon.bos.entity.BosResult;
import com.sungeon.bos.entity.base.SupplierEntity;
import org.springframework.stereotype.Repository;

/**
 * @author 陈苏洲
 * @date 2023-8-30
 **/
@Repository
public interface ISupplierDao {

	// 根据编号查询指定的供应商
	SupplierEntity querySupplierByCode(String supplierCode);

	// 根据编号查询指定的供应商id
	Long querySupplierIdByCode(String supplierCode);

	// 添加供应商
	Long addSupplier(SupplierEntity supplierEntity);


	BosResult callSupplierAC(Long supplierId);
}
