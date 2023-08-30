package com.sungeon.bos.service;

import com.sungeon.bos.entity.base.RetailEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Service
public interface IRetailService {

	List<RetailEntity> syncBsijaRetail(String docNo, int page, int pageSize);

}
