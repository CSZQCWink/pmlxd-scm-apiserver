package com.sungeon.bos.service;

import com.sungeon.bos.entity.base.AttributeEntity;
import com.sungeon.bos.entity.base.AttributeValueEntity;
import com.sungeon.bos.entity.base.DimEntity;
import com.sungeon.bos.entity.base.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Service
public interface IProductService {

	Integer addProduct(List<ProductEntity> products);

	List<DimEntity> syncPmilaDim(String dimFlag, String pmlDimFlag, boolean isUseCode, String startTime, String dimName,
	                             int page, int pageSize);

	List<AttributeValueEntity> syncPmilaColor(String startTime, String colorName, int page, int pageSize);

	List<AttributeValueEntity> syncPmilaSize(String startTime, String sizeName, int page, int pageSize);

	/**
	 * @title: syncPmilaProduct
	 * @author: 陈苏洲
	 * @description: 同步款号
	 * @param: [startTime, productCode, page, pageSize]
	 * @return: java.util.List<com.sungeon.bos.entity.base.ProductEntity>
	 * @date: 2024/1/9 11:22
	 **/
	List<ProductEntity> syncPmilaProduct(String startTime, String productCode, int page, int pageSize);

	List<AttributeEntity> syncPmilaAttribute(String startTime,String attributeName, int page, int pageSize);

}
