package com.sungeon.bos.service;

import com.burgeon.framework.restapi.response.ObjectSubmitResponse;
import com.sungeon.bos.entity.base.InventoryEntity;
import com.sungeon.bos.entity.base.ItemEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Service
public interface IStockService {

	ObjectSubmitResponse dealBsijaOut(String bsijaDocNo, String billType, List<ItemEntity> items);

	ObjectSubmitResponse dealBsijaIn(String bsijaDocNo, String billType);

	List<InventoryEntity> syncBsijaInventory(String docNo, int page, int pageSize);

}
