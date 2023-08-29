package com.sungeon.bos.dao.impl;

import com.sungeon.bos.core.annotation.SgExceptionField;
import com.sungeon.bos.core.exception.InsertFailException;
import com.sungeon.bos.core.exception.ProcedureErrorException;
import com.sungeon.bos.core.exception.QueryFailException;
import com.sungeon.bos.core.exception.UpdateFailException;
import com.sungeon.bos.dao.IStoreDao;
import com.sungeon.bos.entity.BosResult;
import com.sungeon.bos.entity.base.StoreEntity;
import com.sungeon.bos.mapper.IStoreMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author 刘国帅
 * @date 2022-1-19
 **/
@Repository("storeDao")
public class StoreDaoImpl implements IStoreDao {

    @Resource
    private IStoreMapper storeMapper;

    @SgExceptionField(exception = QueryFailException.class)
    @Override
    public StoreEntity queryStoreByCode(String storeCode) {
        return storeMapper.queryStoreByCode(storeCode);
    }

    @SgExceptionField(exception = QueryFailException.class)
    @Override
    public StoreEntity queryStoreBySrcCode(String storeCode) {
        return storeMapper.queryStoreBySrcCode(storeCode);
    }

    @SgExceptionField(exception = InsertFailException.class)
    @Override
    public Integer insertStore(StoreEntity store) {
        return storeMapper.insertStore(store);
    }

    @SgExceptionField(exception = ProcedureErrorException.class)
    @Override
    public BosResult callStoreAc(Long storeId) {
        BosResult result = new BosResult();
        storeMapper.callStoreAc(storeId);
        result.setCode(1);
        result.setMessage("SUCCESS");
        return result;
    }

    @SgExceptionField(exception = UpdateFailException.class)
    @Override
    public Integer updateStore(StoreEntity store) {
        return storeMapper.updateStore(store);
    }

    @SgExceptionField(exception = QueryFailException.class)
    @Override
    public Long queryEmployeeIdByName(String employeeName, Long storeId) {
        return storeMapper.queryEmployeeIdByName(employeeName, storeId);
    }

}
