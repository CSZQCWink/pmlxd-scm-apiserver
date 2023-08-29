package com.sungeon.bos.dao;

import com.sungeon.bos.entity.BosResult;
import com.sungeon.bos.entity.base.StoreEntity;
import org.springframework.stereotype.Repository;

/**
 * @author 刘国帅
 * @date 2022-1-19
 **/
@Repository
public interface IStoreDao {

    StoreEntity queryStoreByCode(String storeCode);

    StoreEntity queryStoreBySrcCode(String storeCode);

    Integer insertStore(StoreEntity store);

    BosResult callStoreAc(Long storeId);

    Integer updateStore(StoreEntity store);

    Long queryEmployeeIdByName(String employeeName, Long storeId);

}
