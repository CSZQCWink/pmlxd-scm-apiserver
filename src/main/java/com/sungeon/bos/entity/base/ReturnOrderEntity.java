package com.sungeon.bos.entity.base;

import java.io.Serializable;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSONObject;


@Data
@Alias("ReturnOrder")
public class ReturnOrderEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String returnOrderid = null; // 电商单号ID
	private String returnOrdersn = null; // 电商单号（平台单号）

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
