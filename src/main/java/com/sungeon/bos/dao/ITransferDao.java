package com.sungeon.bos.dao;

import com.sungeon.bos.entity.base.TransferEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Repository
public interface ITransferDao {

    List<TransferEntity> queryTransferOutList(String docNo, int beg, int end);

    Integer updateTransferSyncStatus(Long transferId, String status, String bsijaNo, String message);

}
