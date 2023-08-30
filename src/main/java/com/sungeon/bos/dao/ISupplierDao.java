package com.sungeon.bos.dao;

import org.springframework.stereotype.Repository;

/**
 * @author 刘国帅
 * @date 2019-8-9
 **/
@Repository
public interface ISupplierDao {

	Long querySupplierIdByCode(String supplierCode);

}
