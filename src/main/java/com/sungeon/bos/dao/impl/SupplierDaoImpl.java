package com.sungeon.bos.dao.impl;

import com.sungeon.bos.core.annotation.SgExceptionField;
import com.sungeon.bos.core.exception.ProcedureErrorException;
import com.sungeon.bos.dao.ISupplierDao;
import com.sungeon.bos.entity.BosResult;
import com.sungeon.bos.entity.base.SupplierEntity;
import com.sungeon.bos.mapper.ISupplierMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-8-9
 **/
@Repository("supplierDao")
public class SupplierDaoImpl extends BaseDaoImpl implements ISupplierDao {

	@Resource
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

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callSupplierAC(Long supplierId) {
		BosResult result = new BosResult();
		supplierMapper.callSupplierAC(supplierId);
		result.setCode(1);
		result.setMessage("SUCCESS");
		return result;
	}

}
