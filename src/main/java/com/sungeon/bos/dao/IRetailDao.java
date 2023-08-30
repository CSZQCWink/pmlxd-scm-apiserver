package com.sungeon.bos.dao;

import com.sungeon.bos.entity.base.ItemEntity;
import com.sungeon.bos.entity.base.PayItemEntity;
import com.sungeon.bos.entity.base.RetailEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Repository
public interface IRetailDao {

	List<RetailEntity> queryRetailList(String docNo, int beg, int end);

	List<ItemEntity> queryRetailItemList(Long retailId);

	List<PayItemEntity> queryRetailPayItemList(Long retailId);

	Integer updateRetailSyncStatus(Long retailId, String status, String bsijaNo, String message);

}
