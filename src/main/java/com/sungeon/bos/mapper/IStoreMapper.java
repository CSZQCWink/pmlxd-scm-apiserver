package com.sungeon.bos.mapper;

import com.sungeon.bos.entity.base.StoreEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 刘国帅
 * @date 2022-1-19
 **/
@Mapper
public interface IStoreMapper {

    StoreEntity queryStoreByCode(String storeCode);

    StoreEntity queryStoreBySrcCode(String storeCode);

    Integer insertStore(StoreEntity store);

    void callStoreAc(Long storeId);

    Integer updateStore(StoreEntity store);

    Long queryEmployeeIdByName(@Param("employeeName") String employeeName, @Param("storeId") Long storeId);

}
