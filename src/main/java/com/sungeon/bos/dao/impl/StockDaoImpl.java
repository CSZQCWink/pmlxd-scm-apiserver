package com.sungeon.bos.dao.impl;

import com.sungeon.bos.core.annotation.SgExceptionField;
import com.sungeon.bos.core.exception.QueryFailException;
import com.sungeon.bos.core.exception.UpdateFailException;
import com.sungeon.bos.entity.base.InventoryEntity;
import com.sungeon.bos.mapper.IStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sungeon.bos.dao.IStockDao;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Repository("stockDao")
public class StockDaoImpl extends BaseDaoImpl implements IStockDao {

	@Autowired
	private IStockMapper stockMapper;

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public List<InventoryEntity> queryInventoryList(String docNo, int beg, int end) {
		return stockMapper.queryInventoryList(docNo, beg, end);
	}

	@SgExceptionField(exception = UpdateFailException.class)
	@Override
	public Integer updateInventorySyncStatus(Long inventoryId, String status, String bsijaNo, String message) {
		return stockMapper.updateInventorySyncStatus(inventoryId, status, bsijaNo, message);
	}

}
