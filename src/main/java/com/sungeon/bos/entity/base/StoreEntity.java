package com.sungeon.bos.entity.base;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.apache.ibatis.type.Alias;


@Data
@Alias("Store")
public class StoreEntity {

	@JSONField(serialize = false)
	private Long id;
	private String storeCode;
	private String storeName;
	private String sapCode;
	@JSONField(serialize = false)
	private Long customerId;
	private String customerCode;
	private String customerName;
	private String storeType;
	private String storeKind;
	private String province;
	private String city;
	private String district;
	private String address;
	private String contractor;
	private String phone;
	private String mobile;
	private String longitude;
	private String latitude;
	private String isNegative;
	private String creationTime;
	private String modifiedTime;
	private String isActive;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
