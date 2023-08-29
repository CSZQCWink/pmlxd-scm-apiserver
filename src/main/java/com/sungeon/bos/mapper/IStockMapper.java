package com.sungeon.bos.mapper;

import com.sungeon.bos.entity.base.InventoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Mapper
public interface IStockMapper {

    List<InventoryEntity> queryInventoryList(@Param("docNo") String docNo, @Param("beg") int beg, @Param("end") int end);

    Integer updateInventorySyncStatus(@Param("inventoryId") Long inventoryId, @Param("status") String status,
                                      @Param("bsijaNo") String bsijaNo, @Param("message") String message);

}
