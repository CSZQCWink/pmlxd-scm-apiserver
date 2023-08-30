package com.sungeon.bos.entity.base;

import java.io.Serializable;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSONObject;


@Data
@Alias("Storage")
public class StorageEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long skuId;
	private String sku;
	private Long productId;
	private String product;
	private Integer storeId;
	private String storeCode;
	private String storeName;
	private Integer qty;
	private Integer qtyCan;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
