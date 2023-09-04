package com.sungeon.bos.dao.impl;

import com.sungeon.bos.dao.ISupplierDao;
import com.sungeon.bos.entity.base.SupplierEntity;
import com.sungeon.bos.mapper.ISupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-8-9
 **/
@Repository("supplierDao")
public class SupplierDaoImpl extends BaseDaoImpl implements ISupplierDao {

	@Autowired
	private ISupplierMapper supplierMapper;


	@Override
	public SupplierEntity querySupplierByCode(String supplierCode) {
		return supplierMapper.querySupplierByCode(supplierCode);
	}

	@Override
	public Long querySupplierIdByCode(String supplierCode) {
		return supplierMapper.querySupplierIdByCode(supplierCode);
	}

	@Override
	public Long addSupplier(SupplierEntity supplierEntity) {
		return supplierMapper.addSupplier(supplierEntity);
	}

	@Override
	public Long addSupplierList(List<SupplierEntity> supplierEntityList) {
		return supplierMapper.addSupplierList(supplierEntityList);
	}
}
