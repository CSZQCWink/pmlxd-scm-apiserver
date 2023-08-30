package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;


@Data
@Alias("Retail")
public class RetailEntity {

	private static final long serialVersionUID = 1L;

	private Long id = null;
	private String docNo = null;
	private String sourceNo = null;
	private Integer billDate = null;
	private String customerCode = null;
	private String storeCode = null;
	private String storeName = null;
	private String vipCardNo = null;
	private String vipMobile = null;
	private String vouchers = null;
	private String statusTime = null;
	private Double totAmtList = null;
	private Double totAmtActual = null;
	private String description = null;
	private List<ItemEntity> items;
	private List<PayItemEntity> payItems;

	private Long storeId;
	private Long vipId;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
