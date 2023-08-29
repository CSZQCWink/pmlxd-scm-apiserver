package com.sungeon.bos.mapper;

import com.sungeon.bos.entity.base.AttributeValueEntity;
import com.sungeon.bos.entity.base.DimEntity;
import com.sungeon.bos.entity.base.ProductEntity;
import com.sungeon.bos.entity.base.SkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Mapper
public interface IProductMapper {

    SkuEntity queryProductBySku(String sku);

    SkuEntity queryProductByForCode(String sku);

    SkuEntity queryProductByProductColorSize(@Param("productCode") String productCode,
                                             @Param("colorCode") String colorCode,
                                             @Param("sizeCode") String sizeCode);

    ProductEntity queryProductByCode(String productCode);

    String querySizeGroupNameBySize(String sizeCode);

    Long queryAttributeId(@Param("clr") int clr, @Param("attributeName") String attributeName);

    Long queryDimId(@Param("dimFlag") String dimFlag, @Param("dim") String dim);

    Integer insertDim(DimEntity dim);

    Integer insertProduct(ProductEntity product);

    void callProductAc(Map<String, Object> map);

    Integer updateProduct(ProductEntity product);

    Integer updateProductMedia(@Param("productId") Long productId, @Param("productCode") String productCode);

    Integer insertSku(@Param("skus") List<SkuEntity> skus);

    AttributeValueEntity queryAttributeValue(@Param("clr") int clr, @Param("code") String code,
                                             @Param("attributeId") Long attributeId, @Param("brandId") Long brandId);

    Integer insertAttributeValue(AttributeValueEntity attributeValue);

    void callColorAc(Long colorId);

    void callSizeAc(Long sizeId);

}
