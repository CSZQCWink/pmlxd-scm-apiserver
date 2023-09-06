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

	private Long id;
	private String productCode;
	private String productName;
	private String productEnName;
	private String unit;
	private String sizeGroupName;
	private String supplierCode;
	private String supplierName;
	private Double preCost;
	private Double priceList;
	private List<SkuEntity> skus;

	private String brandCode;
	private String brandName;
	private String yearCode;
	private String yearName;
	private String seasonCode;
	private String seasonName;
	private String bigClassCode;
	private String bigClassName;
	private String classCode;
	private String className;
	private String midClassCode;
	private String midClassName;
	private String smallClassCode;
	private String smallClassName;
	private String genderCode;
	private String genderName;
	private String bandCode;
	private String bandName;
	private String standardCode;
	private String standardName;
	private String securityCategoryCode;
	private String securityCategoryName;

	private Long sizeGroupId;
	private Long supplierId;
	private Long brandId;
	private Long yearId;
	private Long seasonId;
	private Long bigClassId;
	private Long classId;
	private Long midClassId;
	private Long smallClassId;
	private Long genderId;
	private Long bandId;
	private Long standardId;
	private Long securityCategoryId;

	private List<SkuEntity> skuEntities;


	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
