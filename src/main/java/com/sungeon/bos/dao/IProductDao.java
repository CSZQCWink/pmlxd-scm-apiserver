package com.sungeon.bos.dao;

import com.sungeon.bos.entity.BosResult;
import com.sungeon.bos.entity.base.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Repository
public interface IProductDao {

	SkuEntity queryProductBySku(String sku);

	SkuEntity queryProductByForCode(String sku);

	SkuEntity queryProductByProductColorSize(String productCode, String colorCode, String sizeCode);

	ProductEntity queryProductByCode(String productCode);

	String querySizeGroupNameBySize(String sizeCode);

	Long queryAttributeId(int clr, String attributeName);

	Long queryDimId(String dimFlag, String dim);

	Integer insertDim(DimEntity dim);

	Integer insertProduct(ProductEntity product);

	BosResult callProductAc(Long productId);

	Integer updateProduct(ProductEntity product);

	Integer updateProductMedia(Long productId, String productCode);

	Integer insertSku(List<SkuEntity> skus);

	AttributeValueEntity queryAttributeValue(int clr, String code, Long attributeId, Long brandId);

	Integer insertAttributeValue(AttributeValueEntity attributeValue);

	BosResult callColorAc(Long colorId);

	BosResult callSizeAc(Long sizeId);

	Integer insertAttribute(AttributeEntity attribute);

	Long queryASI(Long sizeGroupId,String sizeCode,String colorCode);

	BosResult callSizeGroupAC(Long id);

	BosResult callColorGroupAC(Long id);

	List<AttributeEntity> queryAttribute();

	List<AttributeValueEntity> queryAttributeValueAll();

	AttributeValueEntity queryAttributeValueByName(int clr, String attributeValueName, Long attributeId, Long brandId);

	AttributeValueEntity queryAttributeByCodeAndName(int clr, String attributeValueCode, String attributeValueName, Long attributeId);
}
