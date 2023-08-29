package com.sungeon.bos.mapper;

import com.sungeon.bos.entity.base.ItemEntity;
import com.sungeon.bos.entity.base.PayItemEntity;
import com.sungeon.bos.entity.base.RetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Mapper
public interface IRetailMapper {

    List<RetailEntity> queryRetailList(@Param("docNo") String docNo, @Param("beg") int beg, @Param("end") int end);

    List<ItemEntity> queryRetailItemList(Long retailId);

    List<PayItemEntity> queryRetailPayItemList(Long retailId);

    Integer updateRetailSyncStatus(@Param("retailId") Long retailId, @Param("status") String status,
                                   @Param("bsijaNo") String bsijaNo, @Param("message") String message);

}
