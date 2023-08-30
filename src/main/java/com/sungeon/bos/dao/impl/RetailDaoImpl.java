package com.sungeon.bos.dao.impl;

import com.sungeon.bos.core.annotation.SgExceptionField;
import com.sungeon.bos.core.exception.QueryFailException;
import com.sungeon.bos.core.exception.UpdateFailException;
import com.sungeon.bos.dao.IRetailDao;
import com.sungeon.bos.entity.base.ItemEntity;
import com.sungeon.bos.entity.base.PayItemEntity;
import com.sungeon.bos.entity.base.RetailEntity;
import com.sungeon.bos.mapper.IRetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Repository("retailDao")
public class RetailDaoImpl implements IRetailDao {

	@Autowired
	private IRetailMapper retailMapper;

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public List<RetailEntity> queryRetailList(String docNo, int beg, int end) {
		return retailMapper.queryRetailList(docNo, beg, end);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public List<ItemEntity> queryRetailItemList(Long retailId) {
		return retailMapper.queryRetailItemList(retailId);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public List<PayItemEntity> queryRetailPayItemList(Long retailId) {
		return retailMapper.queryRetailPayItemList(retailId);
	}

	@SgExceptionField(exception = UpdateFailException.class)
	@Override
	public Integer updateRetailSyncStatus(Long retailId, String status, String bsijaNo, String message) {
		return retailMapper.updateRetailSyncStatus(retailId, status, bsijaNo, message);
	}

}
