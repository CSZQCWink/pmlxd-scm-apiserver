package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;


@Alias("Sku")
@Data
public class SkuEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long productId;
	private Long asiId;
	private Long brandId;
	private Long sizeGroupId;
	private String sku;
	private String productCode;
	private String productName;
	private Double priceList;
	private Double priceCost;
	private String intsCode;
	private String forCode;
	private String colorCode;
	private String colorName;
	private String sizeCode;
	private String sizeName;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
