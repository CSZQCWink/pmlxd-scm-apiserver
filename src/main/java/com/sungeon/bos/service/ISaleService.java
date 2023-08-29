package com.sungeon.bos.service;

import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Service
public interface ISaleService {

    List<PurchaseEntity> syncBsijaSale(String startTime, String docNo, int page, int pageSize);

    List<PurchaseReturnEntity> syncBsijaSaleReturn(String startTime, String docNo, int page, int pageSize);

}
