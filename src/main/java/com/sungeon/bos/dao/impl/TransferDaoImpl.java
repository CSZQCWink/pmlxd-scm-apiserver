package com.sungeon.bos.dao.impl;

import com.sungeon.bos.core.annotation.SgExceptionField;
import com.sungeon.bos.core.exception.QueryFailException;
import com.sungeon.bos.core.exception.UpdateFailException;
import com.sungeon.bos.entity.base.TransferEntity;
import com.sungeon.bos.mapper.ITransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sungeon.bos.dao.ITransferDao;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Repository("transferDao")
public class TransferDaoImpl extends BaseDaoImpl implements ITransferDao {

    @Autowired
    private ITransferMapper transferMapper;

    @SgExceptionField(exception = QueryFailException.class)
    @Override
    public List<TransferEntity> queryTransferOutList(String docNo, int beg, int end) {
        return transferMapper.queryTransferOutList(docNo, beg, end);
    }

    @SgExceptionField(exception = UpdateFailException.class)
    @Override
    public Integer updateTransferSyncStatus(Long transferId, String status, String bsijaNo, String message) {
        return transferMapper.updateTransferSyncStatus(transferId, status, bsijaNo, message);
    }

}
