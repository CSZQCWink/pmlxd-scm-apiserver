package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.List;


@Data
@Alias("Product")
public class ProductEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id; // id
	private String productCode; // 款号
	private String productName; // 品名
	private String description2; // 备注
	// 尺寸组
	private Long sizeGroupId;
	private String sizeGroupName;
	// 供应商
	private Long supplierId;
	private String supplierCode;
	private String supplierName;
	private Double preCost; // 采购价
	private Double priceList; // 标准价

	// 品牌相关
	private Long brandId;
	private String brandCode;
	private String brandName;
	// 年份相关
	private Long yearId;
	private String yearCode;
	private String yearName;
	// 季节相关
	private Long seasonId;
	private String seasonCode;
	private String seasonName;
	// 大类相关
	private Long bigClassId;
	private String bigClassCode;
	private String bigClassName;
	// 小类相关
	private Long smallClassId;
	private String smallClassCode;
	private String smallClassName;
	// 材质大类
	private Long materialBigClassId;
	private String materialBigClassCode;
	private String materialBigClassName;
	// 跟型
	private Long HeelTypeId;
	private String HeelTypeCode;
	private String HeelTypeName;
	// 产地
	private Long originPlaceId;
	private String originPlaceCode;
	private String originPlaceName;
	// 质量等级
	private Long qualityGradeId;
	private String qualityGradeCode;
	private String qualityGradeName;
	// 执行标准
	private Long standardId;
	private String standardCode;
	private String standardName;

	private List<SkuEntity> skus;




	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
