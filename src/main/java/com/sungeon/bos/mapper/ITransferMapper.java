package com.sungeon.bos.mapper;

import com.sungeon.bos.entity.base.TransferEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Mapper
public interface ITransferMapper {

    List<TransferEntity> queryTransferOutList(@Param("docNo") String docNo, @Param("beg") int beg, @Param("end") int end);

    Integer updateTransferSyncStatus(@Param("transferId") Long transferId, @Param("status") String status,
                                     @Param("bsijaNo") String bsijaNo, @Param("message") String message);

}
