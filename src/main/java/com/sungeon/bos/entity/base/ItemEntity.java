package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;


@Data
@Alias("Item")
public class ItemEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long mainId;
	private Long productId;
	private String product;
	private String productName;
	private Long skuId;
	private String sku;
	private Long asiId;
	private String colorCode;
	private String colorName;
	private String sizeCode;
	private String sizeName;
	private String brandCode;
	private String brandName;
	private String classCode;
	private String className;
	private String yearCode;
	private String yearName;
	private String seasonCode;
	private String seasonName;
	private String genderCode;
	private String genderName;
	private Integer qty;
	private Integer qtyOut;
	private Integer qtyIn;
	private Double priceList;
	private Double priceActual;
	private Double discount;
	private Double amtActual;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
