package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@Alias("Inventory")
public class InventoryEntity {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String docNo;
	private String sourceNo;
	private Integer billDate;
	private String customerCode;
	private String storeCode;
	private String storeName;
	private String docType;
	private String diffReason;
	private String description;
	private List<ItemEntity> items;

	private Long storeId;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
