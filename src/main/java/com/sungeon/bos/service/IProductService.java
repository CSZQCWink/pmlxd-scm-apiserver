package com.sungeon.bos.service;

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

	List<DimEntity> syncBsijaDim(String dimFlag, String zjhDimFlag, boolean isUseCode, String startTime, String dimName,
	                             int page, int pageSize);

	List<AttributeValueEntity> syncBsijaColor(String startTime, String colorName, int page, int pageSize);

	List<AttributeValueEntity> syncBsijaSize(String startTime, String sizeName, int page, int pageSize);

	List<ProductEntity> syncBsijaProduct(String startTime, String productCode, int page, int pageSize);

}
