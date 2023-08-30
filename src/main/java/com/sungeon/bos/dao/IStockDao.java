package com.sungeon.bos.dao;

import com.sungeon.bos.entity.base.InventoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Repository
public interface IStockDao {

	List<InventoryEntity> queryInventoryList(String docNo, int beg, int end);

	Integer updateInventorySyncStatus(Long inventoryId, String status, String bsijaNo, String message);

}
