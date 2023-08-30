package com.sungeon.bos.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.core.annotation.SgExceptionField;
import com.sungeon.bos.core.exception.*;
import com.sungeon.bos.dao.IProductDao;
import com.sungeon.bos.entity.BosResult;
import com.sungeon.bos.entity.base.AttributeValueEntity;
import com.sungeon.bos.entity.base.DimEntity;
import com.sungeon.bos.entity.base.ProductEntity;
import com.sungeon.bos.entity.base.SkuEntity;
import com.sungeon.bos.mapper.IProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Repository("productDao")
public class ProductDaoImpl extends BaseDaoImpl implements IProductDao {

	@Autowired
	private IProductMapper productMapper;

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public SkuEntity queryProductBySku(String sku) {
		return productMapper.queryProductBySku(sku);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public SkuEntity queryProductByForCode(String sku) {
		return productMapper.queryProductByForCode(sku);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public SkuEntity queryProductByProductColorSize(String productCode, String colorCode, String sizeCode) {
		return productMapper.queryProductByProductColorSize(productCode, colorCode, sizeCode);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public ProductEntity queryProductByCode(String productCode) {
		return productMapper.queryProductByCode(productCode);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public String querySizeGroupNameBySize(String sizeCode) {
		return productMapper.querySizeGroupNameBySize(sizeCode);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public Long queryAttributeId(int clr, String attributeName) {
		return productMapper.queryAttributeId(clr, attributeName);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public Long queryDimId(String dimFlag, String dim) {
		return productMapper.queryDimId(dimFlag, dim);
	}

	@SgExceptionField(exception = InsertFailException.class)
	@Override
	public Integer insertDim(DimEntity dim) {
		return productMapper.insertDim(dim);
	}

	@SgExceptionField(exception = InsertFailException.class)
	@Override
	public Integer insertProduct(ProductEntity product) {
		return productMapper.insertProduct(product);
	}

	@Override
	public BosResult callProductAc(Long productId) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("id", productId);
		productMapper.callProductAc(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = UpdateFailException.class)
	@Override
	public Integer updateProduct(ProductEntity product) {
		return productMapper.updateProduct(product);
	}

	@SgExceptionField(exception = UpdateFailException.class)
	@Override
	public Integer updateProductMedia(Long productId, String productCode) {
		return productMapper.updateProductMedia(productId, productCode);
	}

	@SgExceptionField(exception = InsertFailException.class)
	@Override
	public Integer insertSku(List<SkuEntity> skus) {
		return productMapper.insertSku(skus);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public AttributeValueEntity queryAttributeValue(int clr, String code, Long attributeId, Long brandId) {
		return productMapper.queryAttributeValue(clr, code, attributeId, brandId);
	}

	@SgExceptionField(exception = InsertFailException.class)
	@Override
	public Integer insertAttributeValue(AttributeValueEntity attributeValue) {
		return productMapper.insertAttributeValue(attributeValue);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callColorAc(Long colorId) {
		productMapper.callColorAc(colorId);
		BosResult result = new BosResult();
		result.setCode(1);
		result.setMessage("OK");
		return result;
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callSizeAc(Long sizeId) {
		productMapper.callSizeAc(sizeId);
		BosResult result = new BosResult();
		result.setCode(1);
		result.setMessage("OK");
		return result;
	}

}
